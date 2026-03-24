package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class CountryRequestDTO {

    private String countryCode;

    private String name;

    private String currencyCode;

    private String status;
}
