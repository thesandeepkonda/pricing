package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("hybridTaxService")
@Primary
@RequiredArgsConstructor
public class HybridTaxServiceImpl implements TaxService {

    private final LocalTaxServiceImpl localTaxService;
    private final AvalaraService avalaraService;

    @Override
    public double calculateTax(String countryCode, Double price) {


        if ("US".equalsIgnoreCase(countryCode)) {
            try {
                TaxRequestDTO request = new TaxRequestDTO();
                request.setCountryCode("US");
                request.setZipCode("90015");
                request.setPrice(price);

                return avalaraService.calculateTax(request).getTaxAmount();

            } catch (Exception e) {
                return localTaxService.calculateTax(countryCode, price);
            }
        }

        return localTaxService.calculateTax(countryCode, price);
    }

    @Override
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {
        System.out.println("CountryCode = [" + request.getCountryCode() + "]");

        if ("US".equalsIgnoreCase(request.getCountryCode())) {
            try {
                System.out.println("➡️ Calling Avalara...");
                return avalaraService.calculateTax(request);

            } catch (Exception e) {
                System.out.println("❌ Avalara FAILED → fallback");
                e.printStackTrace();
                return localTaxService.calculateTaxDetails(request);
            }
        }

        System.out.println("➡️ Using Local Tax Service...");
        return localTaxService.calculateTaxDetails(request);
    }



}