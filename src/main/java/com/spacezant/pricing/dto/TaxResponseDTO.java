package com.spacezant.pricing.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TaxResponseDTO {

    private Long price;

    private Long taxRate;

    private Long taxAmount;

    private Long finalPrice;

    private String taxType;
}