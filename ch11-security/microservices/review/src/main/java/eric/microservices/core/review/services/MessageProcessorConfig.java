package eric.microservices.core.review.services;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import eric.api.core.review.Review;
import eric.api.core.review.ReviewApi;
import eric.api.event.Event;
import eric.api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

  private final ReviewApi reviewApi;

  @Autowired
  public MessageProcessorConfig(ReviewApi reviewApi) {
    this.reviewApi = reviewApi;
  }

  @Bean
  public Consumer<Event<Integer, Review>> messageProcessor() {
    return event -> {
      LOG.info("Process message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {

        case CREATE:
          Review review = event.getData();
          LOG.info("Create review with ID: {}/{}", review.getProductId(), review.getReviewId());
          reviewApi.createReview(review).block();
          break;

        case DELETE:
          int productId = event.getKey();
          LOG.info("Delete reviews with ProductID: {}", productId);
          reviewApi.deleteReviews(productId).block();
          break;

        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          LOG.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }

      LOG.info("Message processing done!");
    };
  }
}
