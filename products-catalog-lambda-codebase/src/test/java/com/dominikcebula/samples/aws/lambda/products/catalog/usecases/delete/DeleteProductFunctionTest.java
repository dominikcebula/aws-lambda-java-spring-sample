package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.delete;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.WithEmbeddedDynamoDB;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.EmbeddedDynamoDBDataInitializer;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.EmbeddedDynamoDBTablesInitializer;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.error.ErrorDTOAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithEmbeddedDynamoDB(embeddedDynamoDBInitializers = {EmbeddedDynamoDBTablesInitializer.class, EmbeddedDynamoDBDataInitializer.class})
class DeleteProductFunctionTest {
    @Autowired
    private DeleteProductFunction underTest;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ErrorDTOAssertions errorDTOAssertions;

    @ParameterizedTest
    @Event(value = "events/delete-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldGetProduct(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertProductDeleted(requestEvent);
    }

    @ParameterizedTest
    @Event(value = "events/delete-product-invalid-non-existing-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldReportNonExistingProduct(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @ParameterizedTest
    @Event(value = "events/delete-product-invalid-request-no-id.json", type = APIGatewayProxyRequestEvent.class)
    void shouldReportIncorrectUrlWithoutProductId(APIGatewayProxyRequestEvent requestEvent) {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        errorDTOAssertions.assertErrorMessageInResponseBody(response,
                "Unable to find valid UUID for Product ID in request URL /products/:productId");
    }

    private void assertProductDeleted(APIGatewayProxyRequestEvent requestEvent) {
        assertThat(productRepository.existsById(requestEvent.getPathParameters().get("productId"))).isFalse();
    }
}
