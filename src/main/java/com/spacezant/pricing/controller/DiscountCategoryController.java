package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.discounts.DiscountCategoryRequestDTO;
import com.spacezant.pricing.dto.discounts.DiscountCategoryResponseDTO;
import com.spacezant.pricing.service.DiscountCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount-categories")
@RequiredArgsConstructor
public class DiscountCategoryController {

    private final DiscountCategoryService service;

    @PostMapping
    public ResponseEntity<DiscountCategoryResponseDTO> create(@RequestBody DiscountCategoryRequestDTO request) {
        return ResponseEntity.ok(service.create(request));
    }



    @GetMapping
    public ResponseEntity<List<DiscountCategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

}