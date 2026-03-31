package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportTaxService {

    private final ImportDutyService importDutyService;
    private final LocalTaxServiceImpl localTaxService;

    public TaxResponseDTO calculateImportTax(TaxRequestDTO request) {

        double price = request.getPrice();

        // ✅ Duty
        double duty = importDutyService.calculateImportDuty(request);

        // ✅ Local GST
        TaxResponseDTO tax = localTaxService.calculateTaxDetails(request);

        double taxAmount = tax.getTaxAmount() != null ? tax.getTaxAmount() : 0;

        double finalPrice = price + taxAmount + duty;

        tax.setFinalPrice(finalPrice);
        tax.setImportDuty(duty);

        return tax;
    }
}