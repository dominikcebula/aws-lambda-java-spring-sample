package com.dominikcebula.samples.aws.serverless.product.catalog.db.dao;

import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.ListCrudRepository;

@EnableScan
public interface ProductRepository extends ListCrudRepository<Product, String> {
}
