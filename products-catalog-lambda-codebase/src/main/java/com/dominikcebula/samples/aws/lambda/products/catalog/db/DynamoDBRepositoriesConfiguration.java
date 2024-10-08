package com.dominikcebula.samples.aws.lambda.products.catalog.db;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = {ProductRepository.class})
public class DynamoDBRepositoriesConfiguration {
}
