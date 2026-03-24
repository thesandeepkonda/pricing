package com.spacezant.pricing.dto.product;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class VariantInfo {

    private Long id;
    private String variantName;
    private String skuId;
    private List<Long> attributeValueIds;
    private Boolean status;

    private List<PricingInfo> pricing;

    private List<InventoryInfo> inventory;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}