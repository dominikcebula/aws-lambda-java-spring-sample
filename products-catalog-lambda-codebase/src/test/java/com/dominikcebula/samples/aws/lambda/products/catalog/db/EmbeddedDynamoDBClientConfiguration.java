package com.dominikcebula.samples.aws.lambda.products.catalog.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.utils.EmbeddedDynamoDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedDynamoDBClientConfiguration {
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return new EmbeddedDynamoDBClientFactory()
                .create();
    }
}
