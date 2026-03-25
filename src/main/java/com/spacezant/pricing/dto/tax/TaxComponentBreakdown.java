package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxComponentBreakdown {

    private String componentName;
    private Double percentage;
    private Double amount;
}

