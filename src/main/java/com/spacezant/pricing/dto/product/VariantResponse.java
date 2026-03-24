package com.spacezant.pricing.dto.product;

import lombok.Data;
import java.util.List;

@Data
public class VariantResponse {

    private Long variantId;

    private String sku;

    private List<PricingResponse> pricing;
}
