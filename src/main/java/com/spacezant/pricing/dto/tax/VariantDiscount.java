package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantDiscount {

    private String discountName;
    private String discountType;
    private Double discountValue;
    private Double maxDiscount;
}
