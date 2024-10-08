package com.dominikcebula.samples.aws.lambda.products.catalog.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.EmbeddedDynamoDBInitializer;

public class EmbeddedDynamoDBTablesInitializer implements EmbeddedDynamoDBInitializer {
    @Override
    public void initialize(AmazonDynamoDB amazonDynamoDB) {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("products")
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH)
                )
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S)
                )
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        amazonDynamoDB.createTable(createTableRequest);
    }
}
