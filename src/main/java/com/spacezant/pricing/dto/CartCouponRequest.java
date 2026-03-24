package com.spacezant.pricing.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartCouponRequest {

    private List<CartItemDTO> items;

    private String couponCode;   // optional
}