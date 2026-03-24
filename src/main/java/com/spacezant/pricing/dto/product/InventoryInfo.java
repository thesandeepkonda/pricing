package com.spacezant.pricing.dto.product;

import lombok.Data;

@Data
public class InventoryInfo {

    private String inventoryId;

    private int availableQuantity;
    private int reservedQuantity;

    private long lastUpdated;
}
