package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class PricingResponseDTO {

    private Long variantId;

    private Double originalPrice;

    private Double discountAmount;

    private Double finalPrice;

    private String appliedDiscountName;

    private Long discountId;
}