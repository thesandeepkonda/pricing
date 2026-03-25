package com.spacezant.pricing.dto.tax;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxBreakdown {

    private String taxName;   // CGST / SGST / SALES_TAX
    private Double percentage;
    private Double amount;
}
