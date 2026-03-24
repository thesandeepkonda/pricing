package com.spacezant.pricing.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingDetailResponse {

    private Long variantId;

    private String countryCode;
    private String currency;

    private Double basePrice;

    private Double discount;

    private Double priceAfterDiscount;

    private Double tax;

    private Double finalPrice;
}
