package eric.microservices.core.product.services;

import eric.api.core.product.ProductService;
import eric.api.core.product.Product;
import eric.api.exceptions.InvalidInputException;
import eric.api.exceptions.NotFoundException;
import eric.util.http.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) {
            LOG.debug("product<1 throw InvalidInputException");
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        if (productId == 13) {
            LOG.debug("product=13 throw NotFoundException");
            throw new NotFoundException("No product found for productId: " + productId);
        }
        LOG.debug("/product return the found product for productId={}", productId);
        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }
}