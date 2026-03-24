package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.discounts.FinalDiscountPriceResponseDTO;
import com.spacezant.pricing.dto.product.PricingDetailResponse;
import com.spacezant.pricing.dto.product.ProductCreateRequest;
import com.spacezant.pricing.dto.product.ProductCreateResponse;
import com.spacezant.pricing.dto.product.VariantDetailResponseDTO;
import com.spacezant.pricing.service.VariantCountryService;
import com.spacezant.pricing.service.VariantDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variant-pricing")
@RequiredArgsConstructor
public class VariantCountryController {

    private final VariantCountryService variantCountryService;
    private final VariantDiscountService discountService;
    // ✅ 1. CREATE PRODUCT (NEW)
    @PostMapping("/create")
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestBody ProductCreateRequest request) {

        return ResponseEntity.ok(
                variantCountryService.createProductResponse(request)
        );
    }
    // ✅ 1. GET FINAL PRICE (discount + tax)
//    @GetMapping("/final-price")
//    public ResponseEntity<FinalDiscountPriceResponseDTO> getFinalPrice(
//            @RequestParam Long variantId,
//            @RequestParam Long countryId) {
//
//        return ResponseEntity.ok(
//                discountService.getFinalPrice(variantId, countrycode)
//        );
//    }

    // ✅ 2. GET FULL PRICING DETAILS
    @GetMapping("/{variantId}")
    public ResponseEntity<PricingDetailResponse> getPricingDetails(
            @PathVariable Long variantId,
            @RequestParam String countryCode) {

        return ResponseEntity.ok(
                variantCountryService.getPricingDetails(variantId, countryCode)
        );
    }
    @GetMapping("/variant-details")
    public ResponseEntity<VariantDetailResponseDTO> getVariantDetails(
            @RequestParam Long variantId,
            @RequestParam String countryCode) {

        return ResponseEntity.ok(
                discountService.getVariantDetails(variantId, countryCode)
        );
    }
}