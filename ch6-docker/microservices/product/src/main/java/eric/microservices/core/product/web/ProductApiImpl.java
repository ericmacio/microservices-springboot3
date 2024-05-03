package eric.microservices.core.product.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

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
    public Product postProduct(Product body) {
        LOG.info("postProduct has been called");
        try {
            Product newProduct = productService.createProduct(body);
            return newProduct;
        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
        }
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        Product product = productService.getProduct(productId);
        return product;
    }

    @Override
    public void deleteProduct(int productId) {
        productService.deleteProduct(productId);
    }
}