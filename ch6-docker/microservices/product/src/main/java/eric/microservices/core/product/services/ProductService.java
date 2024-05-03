package eric.microservices.core.product.services;

import eric.api.core.product.Product;

public interface ProductService {

    Product createProduct(Product body);

    Product getProduct(int productId);

    void deleteProduct(int productId);
}