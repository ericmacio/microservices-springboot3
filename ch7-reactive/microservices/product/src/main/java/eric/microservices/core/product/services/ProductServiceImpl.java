package eric.microservices.core.product.services;

import static java.util.logging.Level.FINE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;

import eric.api.core.product.Product;
import eric.api.exceptions.InvalidInputException;
import eric.api.exceptions.NotFoundException;
import eric.util.http.ServiceUtil;
import eric.microservices.core.product.persistence.ProductEntity;
import eric.microservices.core.product.persistence.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Product> createProduct(Product body) {
        ProductEntity entity = mapper.apiToEntity(body);
        Mono<Product> newEntity = repository.save(entity)
            .log(LOG.getName(), FINE)
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
            .map(e -> mapper.entityToApi(e));
        return newEntity;
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log(LOG.getName(), FINE)
            .map(e -> mapper.entityToApi(e))
            .map(e -> setServiceAddress(e));
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        return repository.findByProductId(productId).log(LOG.getName(), FINE).map(e -> repository.delete(e)).flatMap(e -> e);
    }

    private Product setServiceAddress(Product e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}