package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.CountryRequestDTO;
import com.spacezant.pricing.dto.CountryResponseDTO;
import com.spacezant.pricing.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService service;

    @PostMapping
    public ResponseEntity<CountryResponseDTO> create(
            @RequestBody CountryRequestDTO request) {

        return ResponseEntity.ok(service.createCountry(request));
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDTO>> getAll() {

        return ResponseEntity.ok(service.getAllCountries());
    }
}
