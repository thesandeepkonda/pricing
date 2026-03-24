package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class FinalDiscountPriceResponseDTO {

    private Long variantId;

    private Long basePrice;

    private String discountType;

    private Long discountValue;

    private Long finalPrice;

    private String currency;
}