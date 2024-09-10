package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.list;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class ListProductsFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ListProductsUseCase listProductsUseCase;
    private final JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        List<Product> products = listProductsUseCase.execute();
        String productsJson = jsonConverter.asJson(products);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.OK)
                .withHeaders(Map.of(
                        CONTENT_TYPE, APPLICATION_JSON_VALUE
                ))
                .withBody(productsJson);
    }
}
