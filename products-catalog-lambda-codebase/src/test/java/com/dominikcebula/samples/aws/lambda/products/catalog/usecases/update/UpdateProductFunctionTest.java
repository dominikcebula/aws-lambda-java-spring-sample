package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.update;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.WithEmbeddedDynamoDB;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.EmbeddedDynamoDBDataInitializer;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.EmbeddedDynamoDBTablesInitializer;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.json.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@WithEmbeddedDynamoDB(embeddedDynamoDBInitializers = {EmbeddedDynamoDBTablesInitializer.class, EmbeddedDynamoDBDataInitializer.class})
class UpdateProductFunctionTest {
    @Autowired
    private UpdateProductFunction underTest;
    @Autowired
    private JsonConverter jsonConverter;
    @Autowired
    private ProductRepository productRepository;

    @ParameterizedTest
    @Event(value = "events/update-existing-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldUpdateExistingProduct(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        Product createdProduct = readProduct(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getHeaders()).containsEntry(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        assertCreatedProduct(createdProduct);
        assertStoredProduct(createdProduct);
    }

    @ParameterizedTest
    @Event(value = "events/update-non-existing-product.json", type = APIGatewayProxyRequestEvent.class)
    void shouldCreateNewProduct(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);

        Product createdProduct = readProduct(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeaders()).containsEntry(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        assertCreatedProduct(createdProduct);
        assertStoredProduct(createdProduct);
    }

    private Product readProduct(APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        return jsonConverter.fromJson(response.getBody(), Product.class);
    }

    private void assertCreatedProduct(Product createdProduct) {
        assertThat(createdProduct.getId()).isNotBlank();
        assertThat(createdProduct.getName()).isEqualTo("Wireless Bluetooth Headphones");
        assertThat(createdProduct.getDescription()).isEqualTo("Over-ear noise-cancelling headphones with 30-hour battery life, built-in mic, and deep bass.");
        assertThat(createdProduct.getCategory()).isEqualTo("Electronics");
        assertThat(createdProduct.getSku()).isEqualTo("WH-12345-BT");
        assertThat(createdProduct.getPrice()).isEqualTo(new BigDecimal("79.99"));
    }

    private void assertStoredProduct(Product createdProduct) {
        assertThat(productRepository.findById(createdProduct.getId()))
                .isEqualTo(Optional.of(createdProduct));
    }
}
