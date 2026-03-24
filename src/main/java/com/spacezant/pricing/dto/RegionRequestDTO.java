package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class RegionRequestDTO {

    private Long countryId;

    private String regionName;

    private String regionCode;

    private String regionType;

    private String status;
}
