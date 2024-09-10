package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.delete;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product.ProductIdHandler;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import com.dominikcebula.samples.aws.serverless.product.catalog.usecases.delete.DeleteProductUseCase.DeleteProductUseCaseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DeleteProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductIdHandler productIdHandler;
    private final JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        Optional<String> productId = productIdHandler.getProductId(requestEvent);
        if (productId.isEmpty()) {
            return productIdHandler.getNonExistingProductIdResponse();
        }

        DeleteProductUseCaseResult useCaseResult = deleteProductUseCase.execute(productId.get());
        return switch (useCaseResult) {
            case PRODUCT_DELETED -> new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.NO_CONTENT);
            case PRODUCT_NOT_FOUND -> new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.NOT_FOUND);
        };
    }
}
