package com.spacezant.pricing.dto.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartRequest {
    private List<CartItemRequest> items;
    private String couponCode;
    private String countryCode;
    private Long regionId;

    private Long userId;
}