package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.discounts.FinalDiscountPriceResponseDTO;
import com.spacezant.pricing.dto.product.*;
import com.spacezant.pricing.service.VariantCountryService;
import com.spacezant.pricing.service.VariantDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/variant-pricing")
@RequiredArgsConstructor
public class VariantCountryController {

    private final VariantCountryService variantCountryService;
    private final VariantDiscountService discountService;

    @GetMapping("/variant-details")
    public ResponseEntity<VariantDetailResponseDTO> getVariantDetails(
            @RequestParam Long variantId,
            @RequestParam String countryCode) {

        return ResponseEntity.ok(
                discountService.getVariantDetails(variantId, countryCode)
        );
    }
    @PostMapping("/variant-pricing/bulk")
    public ResponseEntity<Map<Long, PricingInfo>> getBulkPricing(
            @RequestBody List<Long> variantIds,
            @RequestParam("countryCode") String countryCode) {

        return ResponseEntity.ok(
                discountService.getBulkPricing(variantIds, countryCode)
        );
    }
}