package com.spacezant.pricing.dto.tax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxClassificationRequest {
    private String countryCode;
    private String hsnCode;
    private String description;
    private String externalTaxCode;
}