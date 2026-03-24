package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class PricingInfo {

    private String countryCode; // IN, US, DE
    private String currency; // INR, USD, EUR

    private Long basePrice;
    private double discount;
    private Long finalPrice;

    private List<Long> discountId;

    private Boolean expoAllowed;
    private Boolean impoAllowed;

    private long startFrom;
    private long endTo;

    private boolean active;
}
