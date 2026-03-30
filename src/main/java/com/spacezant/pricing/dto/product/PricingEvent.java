package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PricingEvent {

    private String countryCode;
    private String currency;
    private Double basePrice;
    private Double discount;
    private Double finalPrice;
    private Long taxClassificationId;

    private List<Long> discountIds;

    private Boolean expoAllowed;
    private Boolean impoAllowed;

    private LocalDateTime startFrom;
    private LocalDateTime endTo;

    private Boolean active;

}



