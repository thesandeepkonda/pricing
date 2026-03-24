package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class VariantPricingEvent {

    private Long variantId;
    private Long mrp;

    private Long discountAmount;
    private Long finalAmount;

    private String discountName; // ✅ ADD THIS

    private String currency;
    private String countryCode;
}