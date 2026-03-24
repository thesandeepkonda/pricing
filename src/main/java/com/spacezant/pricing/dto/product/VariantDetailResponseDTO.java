package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class VariantDetailResponseDTO {

    private Long variantId;
    private Double mrp;

    private String discountName;
    private Double discountAmount;

    private Double finalPrice;
    private String countryCode;
    private String currency;
}