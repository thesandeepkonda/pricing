package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PricingInfo {

    private String countryCode; // IN, US, DE
    private String currency; // INR, USD, EUR

    private Double basePrice;
    private Double discount;
    private Double finalPrice;

    private List<Long> discountId;

    private Boolean expoAllowed;
    private Boolean impoAllowed;

    private Long Tax_classificationId; //tax_classification_id

    private LocalDateTime startFrom;
    private LocalDateTime endTo;

    private boolean active;

}
