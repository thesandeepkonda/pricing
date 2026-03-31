package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxFacadeService implements TaxService {

    private final AvalaraService avalaraService;
    private final LocalTaxServiceImpl localTaxService;
    private final ImportTaxService importTaxService;

    @Override
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {

        String country = request.getCountryCode();

        System.out.println("CountryCode = [" + country + "]");

        // 🇺🇸 US → Avalara
        if ("US".equalsIgnoreCase(country)) {
            System.out.println("➡️ Avalara Service");
            return avalaraService.calculateTax(request);
        }

        // 🌍 IMPORT → custom duty
        boolean isImport = request.getOriginCountry() != null
                && !request.getOriginCountry().equalsIgnoreCase(country);

        if (isImport) {
            System.out.println("➡️ Import Duty Service");
            return importTaxService.calculateImportTax(request);
        }

        // 🇮🇳 LOCAL GST
        System.out.println("➡️ Local Tax Service");
        return localTaxService.calculateTaxDetails(request);
    }

    @Override
    public double calculateTax(String countryCode, Double price) {

        TaxRequestDTO request = new TaxRequestDTO();
        request.setCountryCode(countryCode);
        request.setOriginCountry(countryCode);
        request.setPrice(price);

        return calculateTaxDetails(request).getTaxAmount();
    }
}