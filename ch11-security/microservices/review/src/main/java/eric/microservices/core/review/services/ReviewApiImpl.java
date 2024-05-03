package eric.microservices.core.review.services;

import static java.util.logging.Level.FINE;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

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
  private final Scheduler jdbcScheduler;

  @Autowired
  public ReviewApiImpl(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
    this.jdbcScheduler = jdbcScheduler;
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Mono<Review> createReview(Review body) {
    LOG.debug("createReview called");
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    return Mono.fromCallable(() -> internalCreateReview(body))
      .subscribeOn(jdbcScheduler);
  }

  private Review internalCreateReview(Review body) {
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
  public Flux<Review> getReviews(int productId) {
    LOG.debug("getReviews called with productId {}", productId);
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    return Mono.fromCallable(() -> internalGetReviews(productId))
      .flatMapMany(Flux::fromIterable)
      .log(LOG.getName(), FINE)
      .subscribeOn(jdbcScheduler);
  }

  private List<Review> internalGetReviews(int productId) {
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));
    LOG.debug("Response size: {}", list.size());
    return list;
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
  }

  private void internalDeleteReviews(int productId) {
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}
