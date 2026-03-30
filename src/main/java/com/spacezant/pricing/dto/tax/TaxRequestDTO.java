package com.spacezant.pricing.dto.tax;

import lombok.Data;

@Data
public class TaxRequestDTO {

    private Long taxClassificationId;
    private String countryCode; ;
    private Long regionId; // optional
    private Double price;

    private String ZipCode;
    private String city;
    private String state;
    private String addressLine1;

}
