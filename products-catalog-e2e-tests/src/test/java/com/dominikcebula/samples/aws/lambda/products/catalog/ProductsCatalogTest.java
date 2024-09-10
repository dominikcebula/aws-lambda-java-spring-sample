package com.dominikcebula.samples.aws.lambda.products.catalog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksRequest;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksResponse;
import software.amazon.awssdk.services.cloudformation.model.Output;
import software.amazon.awssdk.services.cloudformation.model.Stack;

import java.util.List;

public class ProductsCatalogTest {
    private static String apiEndpoint;

    @BeforeAll
    static void setUp() {
        apiEndpoint = getApiEndpoint();
    }

    @Test
    void shouldCreateAndRetrieveProduct() {
        // TODO
    }

    private static String getApiEndpoint() {
        try (CloudFormationClient cloudFormationClient = CloudFormationClient.create()) {
            DescribeStacksResponse response = cloudFormationClient.describeStacks(DescribeStacksRequest.builder()
                    .stackName("products-catalog-lambda-app")
                    .build());

            return response.stacks().stream()
                    .filter(stack -> stack.stackName().equals("products-catalog-lambda-app"))
                    .map(Stack::outputs)
                    .flatMap(List::stream)
                    .map(Output::outputKey)
                    .filter(s -> s.equals("ApiEndpoint"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Unable to find API Endpoint for testing based on Cloud Formation Stack"));
        }
    }
}
