package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.get;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.WithEmbeddedDynamoDB;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBConfiguration;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBDataInitializer;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBTablesInitializer;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.error.ErrorDTOAssertions;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@WithEmbeddedDynamoDB(embeddedDynamoDBInitializers = {EmbeddedDynamoDBTablesInitializer.class, EmbeddedDynamoDBDataInitializer.class})
@Import(EmbeddedDynamoDBConfiguration.class)
class GetProductFunctionTest {
    @Autowired
    private GetProductFunction underTest;
    @Autowired
    private JsonConverter jsonConverter;
    @Autowired
    private ErrorDTOAssertions errorDTOAssertions;

    @ParameterizedTest
    @Event(value = "events/get-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldGetProduct(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);
        Product product = readProduct(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getHeaders()).containsEntry(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        assertThat(product.getId()).isEqualTo("0191d843-659a-77c7-ada1-54f7b3f420f1");
        assertThat(product.getName()).isEqualTo("Running Shoes");
        assertThat(product.getDescription()).isEqualTo("Comfortable and lightweight running shoes");
        assertThat(product.getCategory()).isEqualTo("sports");
        assertThat(product.getSku()).isEqualTo("sports-runningshoes-002");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("89.99"));
    }

    @ParameterizedTest
    @Event(value = "events/get-product-invalid-non-existing-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldReportNonExistingProduct(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @ParameterizedTest
    @Event(value = "events/get-product-invalid-request-no-id.json", type = APIGatewayProxyRequestEvent.class)
    void shouldReportIncorrectUrlWithoutProductId(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        errorDTOAssertions.assertErrorMessageInResponseBody(response,
                "Unable to find valid UUID for Product ID in request URL /products/:productId");

    }

    private Product readProduct(APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        return jsonConverter.fromJson(response.getBody(), Product.class);
    }
}
