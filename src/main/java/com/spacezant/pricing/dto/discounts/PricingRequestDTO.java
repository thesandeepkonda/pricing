package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class PricingRequestDTO {

    private Long variantId;

    private String countryCode;

    private Integer quantity;

    private Long price; // in paise
}