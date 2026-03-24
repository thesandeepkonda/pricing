package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.TaxRequestDTO;
import com.spacezant.pricing.dto.TaxResponseDTO;

public interface TaxService {

    Long calculateTax(String countryCode, Long price);

    TaxResponseDTO calculateTaxDetails(TaxRequestDTO request);
}


