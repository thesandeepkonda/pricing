package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;

//public interface TaxService {
//
//    double lcalculateTax(String countryCode, Double price);
//
//    TaxResponseDTO calculateTaxDetails(TaxRequestDTO request);
//
//    double calculateTax(String countryCode, Double price);
//}



public interface TaxService {

    TaxResponseDTO calculateTaxDetails(TaxRequestDTO request);

    double calculateTax(String countryCode, Double price);
}