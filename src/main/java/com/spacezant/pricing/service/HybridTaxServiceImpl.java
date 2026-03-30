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
    private final TaxCloudService taxCloudService;

    @Override
    public double calculateTax(String countryCode, Double price) {

        if ("US".equalsIgnoreCase(countryCode)) {
            try {
                TaxRequestDTO request = new TaxRequestDTO();
                request.setCountryCode("US");
                request.setZipCode("90015");
                request.setPrice(price);

                return taxCloudService.calculateTax(request).getTaxAmount();

            } catch (Exception e) {
                return localTaxService.calculateTax(countryCode, price);
            }
        }

        return localTaxService.calculateTax(countryCode, price);
    }


    @Override
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {

        if ("US".equalsIgnoreCase(request.getCountryCode())) {
            try {
                System.out.println("➡️ Calling TaxCloud...");
                return taxCloudService.calculateTax(request);

            } catch (Exception e) {
                System.out.println("❌ TaxCloud FAILED: " + e.getMessage());
                e.printStackTrace();  // 🔥 IMPORTANT

                return localTaxService.calculateTaxDetails(request);
            }
        }

        return localTaxService.calculateTaxDetails(request);
    }
}