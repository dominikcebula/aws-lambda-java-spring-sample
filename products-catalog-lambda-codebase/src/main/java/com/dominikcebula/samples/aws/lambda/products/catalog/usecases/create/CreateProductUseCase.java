package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.create;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProductUseCase {
    private final ProductRepository productRepository;

    public Product execute(ProductDTO productDTO) {
        Product product = new Product(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getCategory(),
                productDTO.getSku(),
                productDTO.getPrice()
        );

        return productRepository.save(product);
    }
}
