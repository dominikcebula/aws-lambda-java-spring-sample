package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.get;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product.ProductIdHandler;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class GetProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final GetProductUseCase getProductUseCase;
    private final ProductIdHandler productIdHandler;
    private final JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        Optional<String> productId = productIdHandler.getProductId(requestEvent);
        if (productId.isEmpty()) {
            return productIdHandler.getNonExistingProductIdResponse();
        }

        Optional<Product> product = getProductUseCase.execute(productId.get());
        if (product.isPresent()) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatusCode.OK)
                    .withHeaders(Map.of(
                            CONTENT_TYPE, APPLICATION_JSON_VALUE
                    ))
                    .withBody(jsonConverter.asJson(product.get()));
        } else {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatusCode.NOT_FOUND);
        }
    }
}
