package com.spacezant.pricing.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxRuleResponse {

    private Long id;

    private String countryCode;

    private Long taxRate;

    private String taxType;

    private Boolean isActive;
}