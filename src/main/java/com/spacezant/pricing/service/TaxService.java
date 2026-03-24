package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.TaxRequestDTO;
import com.spacezant.pricing.dto.TaxResponseDTO;

public interface TaxService {

    double calculateTax(String countryCode, Double price);

    TaxResponseDTO calculateTaxDetails(TaxRequestDTO request);
}


