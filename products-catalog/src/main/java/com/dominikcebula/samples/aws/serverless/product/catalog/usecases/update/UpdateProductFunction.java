package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.update;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product.ProductDTO;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product.ProductIdHandler;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import com.dominikcebula.samples.aws.serverless.product.catalog.usecases.update.UpdateProductUseCase.UpdateProductUseCaseResult;
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
public class UpdateProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final UpdateProductUseCase updateProductUseCase;
    private final ProductIdHandler productIdHandler;
    private final JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        Optional<String> productId = productIdHandler.getProductId(requestEvent);
        if (productId.isEmpty()) {
            return productIdHandler.getNonExistingProductIdResponse();
        }

        ProductDTO productDTO = jsonConverter.fromJson(requestEvent.getBody(), ProductDTO.class);

        UpdateProductUseCaseResult useCaseResult = updateProductUseCase.execute(productId.get(), productDTO);

        return getResponse(useCaseResult);
    }

    private APIGatewayProxyResponseEvent getResponse(UpdateProductUseCaseResult useCaseResult) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        attachResponseHeaders(response);
        attachResponseStatusCode(useCaseResult, response);
        attachResponseBody(useCaseResult, response);

        return response;
    }

    private void attachResponseHeaders(APIGatewayProxyResponseEvent response) {
        response.setHeaders(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE));
    }

    private void attachResponseStatusCode(UpdateProductUseCaseResult useCaseResult, APIGatewayProxyResponseEvent response) {
        switch (useCaseResult.getUpdateMode()) {
            case PRODUCT_CREATED -> response.setStatusCode(HttpStatusCode.CREATED);
            case PRODUCT_UPDATED -> response.setStatusCode(HttpStatusCode.OK);
        }
    }

    private void attachResponseBody(UpdateProductUseCaseResult useCaseResult, APIGatewayProxyResponseEvent response) {
        response.setBody(jsonConverter.asJson(useCaseResult.getProduct()));
    }
}
