package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class CreateTaxRuleRequest {

    private String countryCode;

    private Long taxRate;   // 18 = 18%

    private String taxType;

    private Boolean isActive;
}
