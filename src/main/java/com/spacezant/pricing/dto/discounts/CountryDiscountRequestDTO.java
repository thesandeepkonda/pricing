package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CountryDiscountRequestDTO {

    private Long discountId;

    private Long countryId;

    private String description;

    private String status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}