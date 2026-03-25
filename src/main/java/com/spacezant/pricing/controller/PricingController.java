package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.tax.PricingRequest;
import com.spacezant.pricing.dto.tax.PricingResponse;
import com.spacezant.pricing.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/calculate")
    public ResponseEntity<PricingResponse> calculate(
            @RequestBody PricingRequest request
    ) {
        return ResponseEntity.ok(pricingService.calculatePrice(request));
    }
}