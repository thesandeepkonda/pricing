package com.spacezant.pricing.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PricingEvent {

    private String countryCode;
    private String currency;
   // private Long basePrice;
    private Double mrp;
    private Double discount;
    private Double finalPrice;
    private Long taxClassificationId;
    //private List<Long> discountId;
    private List<Long> discountIds;

    private Boolean expoAllowed;
    private Boolean impoAllowed;

    private LocalDateTime startFrom;
    private LocalDateTime endTo;

    private boolean active;


}



