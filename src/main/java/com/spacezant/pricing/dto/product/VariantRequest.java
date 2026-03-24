package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class VariantRequest {

    private Long variantId;
    private String sku;

    private List<PricingRequest> pricing;
}
