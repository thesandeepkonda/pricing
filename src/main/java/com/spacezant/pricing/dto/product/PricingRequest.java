package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class PricingRequest {

    private String countryCode;
    private String currency;
    private Double basePrice;
    private String status;
}