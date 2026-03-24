package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class VariantDetailResponseDTO {

    private Long variantId;
    private Long mrp;

    private String discountName;
    private Long discountAmount;

    private Long finalPrice;
    private String countryCode;
    private String currency;
}