package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.tax.PricingRequest;
import com.spacezant.pricing.dto.tax.PricingResponse;
import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import com.spacezant.pricing.service.PricingService;
import com.spacezant.pricing.service.TaxFacadeService;
import com.spacezant.pricing.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TaxService taxService;
    private final TaxFacadeService taxFacadeService;


    @PostMapping("/test")
    public TaxResponseDTO test(@RequestBody TaxRequestDTO request) {
        return taxService.calculateTaxDetails(request);
    }

    // 🌍 TEST IMPORT (US → IN)
    @GetMapping("/test/import")
    public TaxResponseDTO testImport() {
        TaxRequestDTO req = new TaxRequestDTO();
        req.setCountryCode("IN");
        req.setOriginCountry("US");
        req.setRegionId("1");
        req.setTaxClassificationId("1");
        req.setPrice(100.0);
        return taxFacadeService.calculateTaxDetails(req);
    }
    @PostMapping("/test/import")
    public TaxResponseDTO testImport(@RequestBody TaxRequestDTO request) {
        return taxFacadeService.calculateTaxDetails(request);
    }
}