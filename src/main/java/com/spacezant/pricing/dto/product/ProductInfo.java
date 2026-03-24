package com.spacezant.pricing.dto.product;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class ProductInfo {
    private Long id;
    private String productName;
    private String description;
    private Long categoryId;
    private Long brandId;
    private String hsCode;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}