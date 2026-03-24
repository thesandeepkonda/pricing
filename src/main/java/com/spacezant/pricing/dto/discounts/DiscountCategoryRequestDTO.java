package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class DiscountCategoryRequestDTO {
    private String categoryType;
    private String description;
    private Boolean active;
}