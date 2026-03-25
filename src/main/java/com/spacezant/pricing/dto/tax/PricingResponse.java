package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PricingResponse {

    private Long variantId;

    private Double basePrice;
    private Integer quantity;
    private Double totalBasePrice;

    private String discountName;
    private Double discountAmount;

    private String couponCode;        // 🔥 NEW
    private Double couponDiscount;    // 🔥 NEW

    private Double priceAfterDiscount;

    private Double totalTaxAmount;
    private List<TaxBreakdown> taxBreakdown;

    private Double finalPrice;
    private Long taxClassificationId;
    private String countryCode;
    private Long regionId;
}

