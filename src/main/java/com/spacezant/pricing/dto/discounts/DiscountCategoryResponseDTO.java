package com.spacezant.pricing.dto.discounts;

import lombok.Data;

@Data
public class DiscountCategoryResponseDTO {

    private Long categoryId;
    private String categoryName;
    private String description;
    private Boolean active;
}