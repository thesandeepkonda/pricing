package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class AddVariantTaxRequest {

    private Long variantId;

    private String countryCode; // "IN", "US"
}