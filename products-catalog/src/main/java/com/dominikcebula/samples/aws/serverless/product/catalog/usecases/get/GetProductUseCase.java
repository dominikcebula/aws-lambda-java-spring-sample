package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.get;

import com.dominikcebula.samples.aws.serverless.product.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetProductUseCase {
    private final ProductRepository productRepository;

    public Optional<Product> execute(String productId) {
        return productRepository.findById(productId);
    }
}
