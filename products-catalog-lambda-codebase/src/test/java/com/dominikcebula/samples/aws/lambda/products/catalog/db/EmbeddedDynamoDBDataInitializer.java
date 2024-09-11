package com.dominikcebula.samples.aws.lambda.products.catalog.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.dominikcebula.amazonaws.dynamodb.embedded.junit.extension.api.EmbeddedDynamoDBInitializer;

import java.util.Map;

public class EmbeddedDynamoDBDataInitializer implements EmbeddedDynamoDBInitializer {
    @Override
    public void initialize(AmazonDynamoDB amazonDynamoDB) {
        amazonDynamoDB.putItem(
                "products",
                Map.of(
                        "id", new AttributeValue().withS("0191d843-659a-76b9-87a3-0cad45006a2c"),
                        "name", new AttributeValue().withS("Smartphone"),
                        "description", new AttributeValue().withS("Latest model with 6.5-inch display and 128GB storage"),
                        "category", new AttributeValue().withS("electronics"),
                        "sku", new AttributeValue().withS("electronics-smartphone-001"),
                        "price", new AttributeValue().withN("699.99")
                )
        );

        amazonDynamoDB.putItem(
                "products",
                Map.of(
                        "id", new AttributeValue().withS("0191d843-659a-77c7-ada1-54f7b3f420f1"),
                        "name", new AttributeValue().withS("Running Shoes"),
                        "description", new AttributeValue().withS("Comfortable and lightweight running shoes"),
                        "category", new AttributeValue().withS("sports"),
                        "sku", new AttributeValue().withS("sports-runningshoes-002"),
                        "price", new AttributeValue().withN("89.99")
                )
        );

        amazonDynamoDB.putItem(
                "products",
                Map.of(
                        "id", new AttributeValue().withS("0191d843-659a-7ea6-a29c-efc965fe4cf6"),
                        "name", new AttributeValue().withS("Dining Table"),
                        "description", new AttributeValue().withS("Wooden dining table with a seating capacity of 6"),
                        "category", new AttributeValue().withS("furniture"),
                        "sku", new AttributeValue().withS("furniture-diningtable-003"),
                        "price", new AttributeValue().withN("299.99")
                )
        );
    }
}
