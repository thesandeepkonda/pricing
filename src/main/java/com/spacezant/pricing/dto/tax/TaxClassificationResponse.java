package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class TaxClassificationResponse {

    private Long id;

    private String countryCode;
    private String countryName;

    private String hsnCode;
    private String description;
    private String externalTaxCode;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


