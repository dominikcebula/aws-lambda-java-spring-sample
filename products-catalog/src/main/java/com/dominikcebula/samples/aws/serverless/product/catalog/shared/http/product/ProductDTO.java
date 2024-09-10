package com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String description;
    private String category;
    private String sku;
    private BigDecimal price;
}
