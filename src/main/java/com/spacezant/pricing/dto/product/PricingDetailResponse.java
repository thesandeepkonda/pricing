package com.spacezant.pricing.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingDetailResponse {

    private Long variantId;

    private String countryCode;
    private String currency;

    private Long basePrice;

    private double discount;

    private double priceAfterDiscount;

    private double tax;

    private Long finalPrice;
}
