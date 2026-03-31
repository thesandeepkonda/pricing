package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor   // ✅ FIX
@AllArgsConstructor  // ✅ FIX
public class TaxRequestDTO {

    private double price;
    private String zipCode;
    private String state;
    private String city;
    private String taxClassificationId;
    private String regionId;
    private String countryCode;
    private String originCountry;
}
