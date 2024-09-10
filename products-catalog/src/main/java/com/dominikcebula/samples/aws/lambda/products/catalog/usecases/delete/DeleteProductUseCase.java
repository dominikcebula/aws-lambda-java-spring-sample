package com.dominikcebula.samples.aws.lambda.products.catalog.usecases.delete;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.dao.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dominikcebula.samples.aws.lambda.products.catalog.usecases.delete.DeleteProductUseCase.DeleteProductUseCaseResult.PRODUCT_DELETED;
import static com.dominikcebula.samples.aws.lambda.products.catalog.usecases.delete.DeleteProductUseCase.DeleteProductUseCaseResult.PRODUCT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {
    private final ProductRepository productRepository;

    public DeleteProductUseCaseResult execute(String productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return PRODUCT_DELETED;
        } else {
            return PRODUCT_NOT_FOUND;
        }
    }

    public enum DeleteProductUseCaseResult {
        PRODUCT_DELETED,
        PRODUCT_NOT_FOUND
    }
}
