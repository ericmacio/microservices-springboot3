package eric.microservices.core.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import eric.api.core.product.Product;
import eric.api.core.product.ProductApi;
import eric.microservices.core.product.services.ProductService;
import eric.api.exceptions.InvalidInputException;

@RestController
public class ProductApiImpl implements ProductApi {
    private static final Logger LOG = LoggerFactory.getLogger(ProductApiImpl.class);
    private final ProductService productService;

    @Autowired
    public ProductApiImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Mono<Product> postProduct(Product body) {
        LOG.info("postProduct has been called");
        try {
            Mono<Product> newProduct = productService.createProduct(body);
            return newProduct;
        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
        }
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        LOG.info("getProduct called with productId {}", productId);
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        Mono<Product> product = productService.getProduct(productId);
        return product;
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        return productService.deleteProduct(productId);
    }
}