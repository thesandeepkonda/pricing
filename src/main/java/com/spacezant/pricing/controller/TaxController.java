package com.spacezant.pricing.controller;

import com.spacezant.pricing.service.TaxFacadeService;
import com.spacezant.pricing.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;
    private final TaxFacadeService taxFacadeService;

    // ✅ FULL TAX BREAKDOWN API
    @PostMapping("/calculate")
    public ResponseEntity<TaxResponseDTO> calculateTax(
            @RequestBody TaxRequestDTO request
    ) {
        TaxResponseDTO response = taxService.calculateTaxDetails(request);
        return ResponseEntity.ok(response);
    }

    // ✅ SIMPLE TAX API (optional)
    @GetMapping("/simple")
    public ResponseEntity<Double> calculateSimpleTax(
            @RequestParam String countryCode,
            @RequestParam Double price
    ) {
        double tax = taxService.calculateTax(countryCode, price);
        return ResponseEntity.ok(tax);
    }

    @PostMapping("/Localcalculate")
    public TaxResponseDTO localCalculate(@RequestBody TaxRequestDTO request) {
        return taxService.calculateTaxDetails(request);  // ✅ MUST RETURN
    }

    // ✅ SIMPLE TEST ENDPOINT
    @GetMapping("/ping")
    public String ping() {
        return "Tax service is running 🚀";
    }

}
