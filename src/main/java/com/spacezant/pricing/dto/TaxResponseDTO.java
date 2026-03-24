package com.spacezant.pricing.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TaxResponseDTO {

    private Double price;

    private Long taxRate;

    private Double taxAmount;

    private Double finalPrice;

    private String taxType;
}