package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class DiscountBreakdownDTO {

    private Long discountId;

    private String discountName;

    private Long calculatedDiscount;

    private String reason; // "BEST_MATCH", "LOW_PRIORITY", etc.
}