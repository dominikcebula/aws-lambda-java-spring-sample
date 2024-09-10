package com.dominikcebula.samples.aws.serverless.product.catalog.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.utils.EmbeddedDynamoDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class EmbeddedDynamoDBConfiguration {
    @Bean
    @Primary
    public AmazonDynamoDB embeddedAmazonDynamoDB() {
        return new EmbeddedDynamoDBClientFactory()
                .create();
    }
}
