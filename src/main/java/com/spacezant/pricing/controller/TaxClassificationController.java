package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.tax.TaxClassificationRequest;
import com.spacezant.pricing.dto.tax.TaxClassificationResponse;
import com.spacezant.pricing.service.TaxClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax-classifications")
@RequiredArgsConstructor
public class TaxClassificationController {

    private final TaxClassificationService service;

    // ✅ POST
    @PostMapping
    public ResponseEntity<TaxClassificationResponse> create(
            @RequestBody TaxClassificationRequest request) {

        return ResponseEntity.ok(service.create(request));
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<TaxClassificationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ GET ALL DESCRIPTIONS
    @GetMapping("/descriptions")
    public ResponseEntity<List<TaxClassificationResponse>> getDescriptions() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<TaxClassificationResponse>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(service.search(keyword));
    }
}