package com.spacezant.pricing.dto.tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponse {

    private Long variantId;

    private Double basePrice;
    private Integer quantity;
    private Double totalBasePrice;

    private String discountName;
    private Double discountAmount;

    private String couponCode;
    private Double couponDiscount;

    private Double priceAfterDiscount;

    private Double totalTaxAmount;
    private List<TaxBreakdown> taxBreakdown;

    private Double finalPrice;
    private Long taxClassificationId;
    private String countryCode;
    private Long regionId;
    private String currency;
}

