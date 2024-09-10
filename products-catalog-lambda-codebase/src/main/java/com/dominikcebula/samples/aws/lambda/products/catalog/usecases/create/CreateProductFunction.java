package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.create;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.product.ProductDTO;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.product.ProductIdHandler;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class CreateProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final CreateProductUseCase createProductUseCase;
    private final ProductIdHandler productIdHandler;
    private final JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        ProductDTO productDTO = jsonConverter.fromJson(requestEvent.getBody(), ProductDTO.class);

        Product createdProduct = createProductUseCase.execute(productDTO);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.CREATED)
                .withHeaders(Map.of(
                        CONTENT_TYPE, APPLICATION_JSON_VALUE,
                        LOCATION, getCreatedProductLocation(createdProduct)
                ))
                .withBody(jsonConverter.asJson(createdProduct));
    }

    private String getCreatedProductLocation(Product createdProduct) {
        return productIdHandler.getProductLocationHeader(createdProduct);
    }
}
