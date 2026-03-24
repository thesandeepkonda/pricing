package com.spacezant.pricing.dto.product;

import lombok.Data;
import java.util.List;

@Data
public class ProductCreateRequest {

    private ProductInfo product;

    private List<VariantEvent> variants;
}