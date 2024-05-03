package eric.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eric.api.composite.product.*;
import eric.api.core.product.Product;
import eric.api.core.recommendation.Recommendation;
import eric.api.core.review.Review;
import eric.api.exceptions.NotFoundException;
import eric.util.http.ServiceUtil;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);
  private final ServiceUtil serviceUtil;
  private ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public ProductAggregate getProduct(int productId) {
    LOG.debug("Call integration.getProduct");
    Product product = integration.getProduct(productId);
    if (product == null) {
      LOG.debug("product is null. Throw NotFoundException exception");
      throw new NotFoundException("No product found for productId: " + productId);
    }
    LOG.debug("Call integration.getRecommendations");
    List<Recommendation> recommendations = integration.getRecommendations(productId);
    LOG.debug("Call integration.getReviews");
    List<Review> reviews = integration.getReviews(productId);
    LOG.debug("Create and return ProductAggregate");
    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  private ProductAggregate createProductAggregate(
    Product product,
    List<Recommendation> recommendations,
    List<Review> reviews,
    String serviceAddress) {

    // 1. Setup product info
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries =
      (recommendations == null) ? null : recommendations.stream()
        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
        .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries = 
      (reviews == null) ? null : reviews.stream()
        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
        .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}
