package com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.product;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Optional;
import java.util.UUID;

import static com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.error.ErrorDTO.errorWithMessage;

@Component
@RequiredArgsConstructor
public class ProductIdHandler {
    private final JsonConverter jsonConverter;

    public Optional<String> getProductId(APIGatewayProxyRequestEvent requestEvent) {
        return Optional.ofNullable(requestEvent.getPathParameters())
                .map(pathParameters -> pathParameters.get("productId"))
                .filter(StringUtils::isNotBlank)
                .filter(ProductIdHandler::isUUID);
    }

    public String getProductLocationHeader(Product product) {
        return "/api/v1/products/" + product.getId();
    }

    public APIGatewayProxyResponseEvent getNonExistingProductIdResponse() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.BAD_REQUEST)
                .withBody(jsonConverter.asJson(
                        errorWithMessage("Unable to find valid UUID for Product ID in request URL /api/v1/products/:productId")));
    }

    private static boolean isUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
