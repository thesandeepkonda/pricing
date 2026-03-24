package com.spacezant.pricing.dto;

import lombok.Data;

@Data
public class CartItemPricingDTO {

    private Long varientId;

    private double unitPrice;

    private int quantity;

    private double totalItemPrice;
}