package com.spacezant.pricing.dto.product;

import lombok.Data;
import java.util.List;

@Data
public class ProductCreateResponse {

    private Long productId;

    private List<VariantResponse> variants;
}
