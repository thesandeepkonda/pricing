package com.spacezant.pricing.dto.tax;

import lombok.Data;

@Data
public class PricingRequest {

    private Long userId;
    private Long variantId;
    private String countryCode;
    private Long regionId;
    private Integer quantity;
    private String couponCode;

    private String ZipCode;
    private String city;
    private String state;
    private String addressLine1;
}