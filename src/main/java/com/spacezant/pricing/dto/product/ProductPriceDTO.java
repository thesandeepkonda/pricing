package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class ProductPriceDTO {
    private Long varientId;
    private double price;

    private Long categoryId; // ✅ ADD THIS
}

