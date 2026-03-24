package com.spacezant.pricing.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartCouponResponseDTO {

    private Double totalBasePrice;

    private Double totalDiscount;

    private Double finalCartPrice;

    private List<CartItemPricingDTO> itemBreakdown;
}