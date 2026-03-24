package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class PricingResponse {

    private String countryCode;

    private String currency;

    private Double basePrice;

    private String status;
}