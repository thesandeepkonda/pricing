package com.spacezant.pricing.dto.discounts;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountRequestDTO {

    private String discountName;

    private String categoryType; // ENUM as String (PERCENTAGE, FIXED...)

    private Long discountValue;
    private Long maxDiscount;

    private String description;

    private Boolean active;

    private Long minOrderAmount;
    private Integer minQuantity;

    private Integer priority;
    private Boolean isStackable;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long categoryId; // DiscountCategory
}