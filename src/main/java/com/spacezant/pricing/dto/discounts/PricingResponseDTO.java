package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class PricingResponseDTO {

    private Long variantId;

    private Long originalPrice;

    private Long discountAmount;

    private Long finalPrice;

    private String appliedDiscountName;

    private Long discountId;
}