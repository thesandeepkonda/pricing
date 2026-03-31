package com.spacezant.pricing.dto.easyship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PricingResponseDTO {

    private double duty;
    private double tax;
    private double finalPrice;
}