package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class TaxRequestDTO {

    private String countryCode;

    private Long price;
}
