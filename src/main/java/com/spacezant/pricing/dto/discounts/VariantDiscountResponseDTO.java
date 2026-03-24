package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VariantDiscountResponseDTO {

    private Long variantDiscountId;

    private String eventName;

    private String status;

    private Integer priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long variantId;
    private String countryCode;

    private Long discountId;
    private String discountName;
}
