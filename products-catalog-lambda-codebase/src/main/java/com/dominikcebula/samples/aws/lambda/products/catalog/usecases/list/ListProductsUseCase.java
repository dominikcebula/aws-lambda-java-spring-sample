package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.list;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListProductsUseCase {
    private final ProductRepository productRepository;

    public List<Product> execute() {
        return productRepository.findAll();
    }
}
