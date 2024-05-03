package eric.microservices.core.product.events;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import eric.api.core.product.Product;
import eric.api.core.product.ProductApi;
import eric.api.event.Event;
import eric.api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig {
  private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
  private final ProductApi productApi;

  @Autowired
  public MessageProcessorConfig(ProductApi productApi) {
    this.productApi = productApi;
  }

  @Bean
  public Consumer<Event<Integer, Product>> messageProcessor() {
    return event -> {
      LOG.info("Process message created at {}...", event.getEventCreatedAt());
      switch (event.getEventType()) {
        case CREATE:
          Product product = event.getData();
          LOG.info("Create product with ID: {}", product.getProductId());
          productApi.postProduct(product).block();
          break;
        case DELETE:
          int productId = event.getKey();
          LOG.info("Delete product with ProductID: {}", productId);
          productApi.deleteProduct(productId).block();
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
