package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.list;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.WithEmbeddedDynamoDB;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBConfiguration;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBDataInitializer;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.EmbeddedDynamoDBTablesInitializer;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@WithEmbeddedDynamoDB(embeddedDynamoDBInitializers = {EmbeddedDynamoDBTablesInitializer.class, EmbeddedDynamoDBDataInitializer.class})
@Import(EmbeddedDynamoDBConfiguration.class)
class ListProductsFunctionTest {
    @Autowired
    private ListProductsFunction underTest;
    @Autowired
    private JsonConverter jsonConverter;

    @ParameterizedTest
    @Event(value = "events/list-products.json", type = APIGatewayProxyRequestEvent.class)
    void shouldListAvailableProducts(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        APIGatewayProxyResponseEvent response = underTest.apply(requestEvent);
        List<Product> products = readProducts(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getHeaders()).containsEntry(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        assertThat(products).containsOnly(
                new Product("0191d843-659a-76b9-87a3-0cad45006a2c", "Smartphone", "Latest model with 6.5-inch display and 128GB storage",
                        "electronics", "electronics-smartphone-001", new BigDecimal("699.99")),
                new Product("0191d843-659a-77c7-ada1-54f7b3f420f1", "Running Shoes", "Comfortable and lightweight running shoes",
                        "sports", "sports-runningshoes-002", new BigDecimal("89.99")),
                new Product("0191d843-659a-7ea6-a29c-efc965fe4cf6", "Dining Table", "Wooden dining table with a seating capacity of 6",
                        "furniture", "furniture-diningtable-003", new BigDecimal("299.99"))
        );
    }

    private List<Product> readProducts(APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        return jsonConverter.listFromJson(response.getBody(), Product.class);
    }
}
