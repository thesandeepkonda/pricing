package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountResult {

    private String discountName;
    private Double discountAmount;
}