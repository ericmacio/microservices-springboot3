package eric.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
public interface ProductCompositeApi {

/**
   * Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/product-composite \
   *   -H "Content-Type: application/json" --data \
   *   '{"productId":123,"name":"product 123","weight":123}'
   *
   * @param body A JSON representation of the new composite product
   */
  @Operation(
    summary = "${api.composite.create-composite-product.description}",
    description = "${api.product-composite.create-composite-product.notes}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
    @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PostMapping(
    value    = "/composite",
    consumes = "application/json")
  Mono<Void> createCompositeProduct(@RequestBody ProductAggregate body);

  /**
   * Sample usage: "curl $HOST:$PORT/product-composite/1".
   *
   * @param productId Id of the product
   * @return the composite product info, if found, else null
   */
  @Operation(
    summary = "${api.composite.get-composite.description}",
    description = "${api.composite.get-composite.notes}"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
    @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
    @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
    @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @GetMapping(
    value = "/composite/{productId}",
    produces = "application/json")
  Mono<ProductAggregate> getCompositeProduct(@PathVariable int productId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/product-composite/1".
   *
   * @param productId Id of the product
   */
  @Operation(
    summary = "${api.composite.delete-composite-product.description}",
    description = "${api.composite.delete-composite-product.notes}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
    @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @DeleteMapping(value = "/composite/{productId}")
  Mono<Void> deleteCompositeProduct(@PathVariable int productId);
}
