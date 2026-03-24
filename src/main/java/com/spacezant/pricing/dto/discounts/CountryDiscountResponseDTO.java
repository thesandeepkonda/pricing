package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CountryDiscountResponseDTO {

    private Long countryDiscountId;

    private Long countryId;
    private String countryCode;

    private String status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long discountId;
    private String discountName;
}
