package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TaxResponseDTO {

    private Double price;
    private Double totalTaxPercentage;
    private Double taxAmount;
    private Double finalPrice;
    private String taxType;
    private List<TaxComponentBreakdown> components;
}
