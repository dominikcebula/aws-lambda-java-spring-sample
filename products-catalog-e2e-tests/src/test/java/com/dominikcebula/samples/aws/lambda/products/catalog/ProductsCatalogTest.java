package com.dominikcebula.samples.aws.lambda.products.catalog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksRequest;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksResponse;
import software.amazon.awssdk.services.cloudformation.model.Output;
import software.amazon.awssdk.services.cloudformation.model.Stack;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsBlankString.blankString;

public class ProductsCatalogTest {
    private static String apiEndpoint;

    @BeforeAll
    static void setUp() {
        apiEndpoint = getApiEndpoint();
    }

    @Test
    void shouldRetrieveAnyListOfProducts() {
        given()
                .baseUri(apiEndpoint)
                .when()
                .get("/products")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(not(blankString())).extract();
    }

    private static String getApiEndpoint() {
        try (CloudFormationClient cloudFormationClient = CloudFormationClient.builder().httpClient(ApacheHttpClient.create()).build()) {
            DescribeStacksResponse response = cloudFormationClient.describeStacks(DescribeStacksRequest.builder()
                    .stackName("products-catalog-lambda-app")
                    .build());

            return response.stacks().stream()
                    .filter(stack -> stack.stackName().equals("products-catalog-lambda-app"))
                    .map(Stack::outputs)
                    .flatMap(List::stream)
                    .filter(output -> output.outputKey().equals("ApiEndpoint"))
                    .map(Output::outputValue)
                    .findFirst()
                    .map(endpoint -> endpoint + "dev")
                    .orElseThrow(() -> new IllegalStateException("Unable to find API Endpoint for testing based on Cloud Formation Stack"));
        }
    }
}
