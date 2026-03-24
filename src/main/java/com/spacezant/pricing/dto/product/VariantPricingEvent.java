package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class VariantPricingEvent {

    private Long variantId;
    private Double mrp;

    private Double discountAmount;
    private Double finalAmount;

    private String discountName; // ✅ ADD THIS

    private String currency;
    private String countryCode;
}