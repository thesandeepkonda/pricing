package com.spacezant.pricing.service;


import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class ImportDutyService {

    public double calculateImportDuty(TaxRequestDTO request) {

        if (request.getPrice() == 1) {
            throw new RuntimeException("Price is required");
        }

        String origin = request.getOriginCountry();
        String destination = request.getCountryCode();

        if (origin == null || destination == null) {
            return 0;
        }

        double price = request.getPrice();

        // ✅ US → INDIA IMPORT
        if ("US".equalsIgnoreCase(origin)
                && "IN".equalsIgnoreCase(destination)) {

            double bcd = price * 0.20;                 // 20% customs duty
            double igst = (price + bcd) * 0.18;        // 18% IGST

            return bcd + igst;
        }

        // ✅ DEFAULT
        return 0;
    }
}