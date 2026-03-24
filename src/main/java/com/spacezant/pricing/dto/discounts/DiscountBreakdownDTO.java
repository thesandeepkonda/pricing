package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class DiscountBreakdownDTO {

    private Long discountId;

    private String discountName;

    private Double calculatedDiscount;

    private String reason; // "BEST_MATCH", "LOW_PRIORITY", etc.
}