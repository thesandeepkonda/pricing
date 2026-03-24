package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.AddVariantTaxRequest;
import com.spacezant.pricing.dto.TaxRequestDTO;
import com.spacezant.pricing.dto.TaxResponseDTO;
import com.spacezant.pricing.service.TaxAdminService;
import com.spacezant.pricing.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;
    private final TaxAdminService taxAdminService;

    // 🔹 Simple calculation
    @GetMapping("/calculate")
    public double calculateTax(
            @RequestParam String countryCode,
            @RequestParam Double price
    ) {
        return taxService.calculateTax(countryCode, price);
    }

    // 🔹 Detailed response
    @PostMapping("/details")
    public TaxResponseDTO calculateTaxDetails(@RequestBody TaxRequestDTO request) {
        return taxService.calculateTaxDetails(request);
    }
    @PostMapping("/apply")
    public ResponseEntity<String> applyTaxToVariant(
            @RequestBody AddVariantTaxRequest request) {

        taxAdminService.applyTaxToVariant(request);

        return ResponseEntity.ok("Tax validated & applied successfully");
    }
}