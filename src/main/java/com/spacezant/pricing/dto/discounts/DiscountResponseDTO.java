package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountResponseDTO {

    private Long discountId;

    private String discountName;

    private String categoryType;

    private Long discountValue;
    private Long maxDiscount;

    private String description;

    private Boolean active;

    private Integer priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}