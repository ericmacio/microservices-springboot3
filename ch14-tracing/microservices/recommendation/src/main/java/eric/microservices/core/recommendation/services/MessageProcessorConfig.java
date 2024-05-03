package eric.microservices.core.recommendation.services;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import eric.api.core.recommendation.Recommendation;
import eric.api.core.recommendation.RecommendationApi;
import eric.api.event.Event;
import eric.api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

  private final RecommendationApi recommendationApi;

  @Autowired
  public MessageProcessorConfig(RecommendationApi recommendationApi) {
    this.recommendationApi = recommendationApi;
  }

  @Bean
  public Consumer<Event<Integer, Recommendation>> messageProcessor() {
    return event -> {

      LOG.info("Process message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {

        case CREATE:
          Recommendation recommendation = event.getData();
          LOG.info("Create recommendation with ID: {}/{}", recommendation.getProductId(), recommendation.getRecommendationId());
          recommendationApi.createRecommendation(recommendation).block();
          break;

        case DELETE:
          int productId = event.getKey();
          LOG.info("Delete recommendations with ProductID: {}", productId);
          recommendationApi.deleteRecommendations(productId).block();
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
