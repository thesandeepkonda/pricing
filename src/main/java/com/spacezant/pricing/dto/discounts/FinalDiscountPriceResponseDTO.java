package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class FinalDiscountPriceResponseDTO {

    private Long variantId;

    private Double basePrice;

    private String discountType;

    private Double discountValue;

    private Double finalPrice;

    private String currency;
}