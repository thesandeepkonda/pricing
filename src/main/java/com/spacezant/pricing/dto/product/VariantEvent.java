package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class VariantEvent {
    private Long id; // 🔥 NOT variantId
    private String variantName;
    private String skuId;
    private Boolean status;
    private List<PricingEvent> pricing;

}