package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;

public interface TaxService {

    TaxResponseDTO calculateTaxDetails(TaxRequestDTO request);

    double calculateTax(String countryCode, Double price);
}

