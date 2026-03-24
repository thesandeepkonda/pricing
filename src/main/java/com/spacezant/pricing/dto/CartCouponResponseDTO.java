package com.spacezant.pricing.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartCouponResponseDTO {

    private Long totalBasePrice;

    private double totalDiscount;

    private double finalCartPrice;

    private List<CartItemPricingDTO> itemBreakdown;
}