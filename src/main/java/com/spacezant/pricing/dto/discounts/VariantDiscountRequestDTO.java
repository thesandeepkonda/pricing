package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VariantDiscountRequestDTO {

    private Long variantCountryId;

    private Long countryDiscountId;

    private String eventName;

    private String description;

    private String status;

    private Integer priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}