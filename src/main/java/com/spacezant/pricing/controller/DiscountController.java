package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.discounts.DiscountRequestDTO;
import com.spacezant.pricing.dto.discounts.DiscountResponseDTO;
import com.spacezant.pricing.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountResponseDTO> createDiscount(
            @RequestBody DiscountRequestDTO dto) {
        return ResponseEntity.ok(discountService.createDiscount(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponseDTO> getDiscount(@PathVariable Long id) {
        return ResponseEntity.ok(discountService.getDiscount(id));
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponseDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponseDTO> updateDiscount(
            @PathVariable Long id,
            @RequestBody DiscountRequestDTO dto) {
        return ResponseEntity.ok(discountService.updateDiscount(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}