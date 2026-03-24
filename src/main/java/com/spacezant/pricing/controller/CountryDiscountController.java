package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.discounts.CountryDiscountRequestDTO;
import com.spacezant.pricing.dto.discounts.CountryDiscountResponseDTO;
import com.spacezant.pricing.service.CountryDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/country-discounts")
@RequiredArgsConstructor
public class CountryDiscountController {

    private final CountryDiscountService service;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<CountryDiscountResponseDTO> create(
            @RequestBody CountryDiscountRequestDTO dto) {

        return ResponseEntity.ok(service.create(dto));
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CountryDiscountResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<CountryDiscountResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}