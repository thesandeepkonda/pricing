package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {
    private ProductInfo productInfo;
    private List<VariantEvent> variants;
}