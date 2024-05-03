package eric.microservices.core.recommendation.services;

import static java.util.logging.Level.FINE;

import eric.api.core.recommendation.RecommendationApi;
import eric.api.core.recommendation.Recommendation;
import eric.api.exceptions.InvalidInputException;
import eric.util.http.ServiceUtil;
import eric.microservices.core.recommendation.persistence.RecommendationEntity;
import eric.microservices.core.recommendation.persistence.RecommendationRepository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
public class RecommendationApiImpl implements RecommendationApi {
  private static final Logger LOG = LoggerFactory.getLogger(RecommendationApiImpl.class);
  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;
  private final ServiceUtil serviceUtil;

  @Autowired
  public RecommendationApiImpl(RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    LOG.debug("createRecommendation called");
    RecommendationEntity entity = mapper.apiToEntity(body);
    Mono<Recommendation> newEntity = repository.save(entity)
      .log(LOG.getName(), FINE)
      .onErrorMap(
        DuplicateKeyException.class,
        ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId()))
      .map(e -> mapper.entityToApi(e));
    return newEntity;
  }

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {
    LOG.debug("getRecommendations called with productId {}", productId);
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    return repository.findByProductId(productId)
      .log(LOG.getName(), FINE)
      .map(e -> mapper.entityToApi(e))
      .map(e -> setServiceAddress(e));
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
    return repository.deleteAll(repository.findByProductId(productId));
  }

  private Recommendation setServiceAddress(Recommendation e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
