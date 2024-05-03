package eric.microservices.core.review.services;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import eric.api.core.review.Review;
import eric.api.core.review.ReviewApi;
import eric.api.exceptions.InvalidInputException;
import eric.util.http.ServiceUtil;
import eric.microservices.core.review.persistence.ReviewEntity;
import eric.microservices.core.review.persistence.ReviewRepository;

@RestController
public class ReviewApiImpl implements ReviewApi {
  private static final Logger LOG = LoggerFactory.getLogger(ReviewApiImpl.class);
  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final ServiceUtil serviceUtil;

  @Autowired
  public ReviewApiImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Review createReview(Review body) {
    LOG.debug("createReview called");
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);
      LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
    }
  }

  @Override
  public List<Review> getReviews(int productId) {
    LOG.debug("getReviews called with productId {}", productId);
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    LOG.debug("getReviews: entityList size: {}", entityList.size());
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));
    LOG.debug("getReviews: response size: {}", list.size());
    return list;
  }

  @Override
  public void deleteReviews(int productId) {
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}
