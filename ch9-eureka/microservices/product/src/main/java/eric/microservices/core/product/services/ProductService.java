package eric.microservices.core.product.services;

import eric.api.core.product.Product;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Product> createProduct(Product body);

    Mono<Product> getProduct(int productId);

    Mono<Void> deleteProduct(int productId);
}