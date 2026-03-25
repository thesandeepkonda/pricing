package com.spacezant.pricing.dto.cart;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long variantId;
    private String countryCode;
    private Long regionId;
    private Integer quantity;
}
