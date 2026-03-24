package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class RegionResponseDTO {

    private Long regionId;

    private Long countryId;

    private String regionName;

    private String regionCode;

    private String regionType;
}