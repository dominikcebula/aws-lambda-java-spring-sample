package com.dominikcebula.samples.aws.lambda.products.catalog.db.dao;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.ListCrudRepository;

@EnableScan
public interface ProductRepository extends ListCrudRepository<Product, String> {
}
