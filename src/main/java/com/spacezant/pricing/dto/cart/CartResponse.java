package com.spacezant.pricing.dto.cart;

import com.spacezant.pricing.dto.tax.PricingResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {

    private List<PricingResponse> items;

    private Double totalBasePrice;
    private Double totalProductDiscount;
    private Double couponDiscount;

    private Double totalTax;
    private Double grandTotal;
}
