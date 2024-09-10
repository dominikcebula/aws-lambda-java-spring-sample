package com.dominikcebula.samples.aws.serverless.product.catalog.usecases.update;

import com.dominikcebula.samples.aws.serverless.product.catalog.db.dao.ProductRepository;
import com.dominikcebula.samples.aws.serverless.product.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product.ProductDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dominikcebula.samples.aws.serverless.product.catalog.usecases.update.UpdateProductUseCase.UpdateMode.PRODUCT_CREATED;
import static com.dominikcebula.samples.aws.serverless.product.catalog.usecases.update.UpdateProductUseCase.UpdateMode.PRODUCT_UPDATED;

@Component
@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProductRepository productRepository;

    public UpdateProductUseCaseResult execute(String productId, ProductDTO productDTO) {
        Product product = new Product(
                productId, productDTO.getName(), productDTO.getDescription(),
                productDTO.getCategory(), productDTO.getSku(), productDTO.getPrice()
        );

        boolean didExist = productRepository.existsById(productId);

        Product savedProduct = productRepository.save(product);

        return new UpdateProductUseCaseResult(
                savedProduct,
                getUpdateMode(didExist)
        );
    }

    private UpdateMode getUpdateMode(boolean didExist) {
        return didExist ? PRODUCT_UPDATED : PRODUCT_CREATED;
    }

    @RequiredArgsConstructor
    @Getter
    public static class UpdateProductUseCaseResult {
        private final Product product;
        private final UpdateMode updateMode;
    }

    public enum UpdateMode {
        PRODUCT_CREATED,
        PRODUCT_UPDATED
    }
}
