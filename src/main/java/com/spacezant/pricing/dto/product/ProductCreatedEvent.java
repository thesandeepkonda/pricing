package com.spacezant.pricing.dto.product;

import lombok.Data;
import java.util.List;


@Data
public class ProductCreatedEvent {
    private Long productId;
    private Long variantId;
    private String skuId;
    private ProductData productData;
}