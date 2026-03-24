package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class CountryResponseDTO {

    private Long countryId;

    private String countryCode;

    private String name;

    private String currencyCode;
}
