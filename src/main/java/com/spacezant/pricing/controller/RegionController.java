package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.RegionRequestDTO;
import com.spacezant.pricing.dto.product.RegionResponseDTO;
import com.spacezant.pricing.repository.RegionRepository;
import com.spacezant.pricing.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;
    private final RegionRepository regionRepository;

    // ✅ CREATE REGION
    @PostMapping
    public ResponseEntity<RegionResponseDTO> createRegion(
            @RequestBody RegionRequestDTO request) {

        RegionResponseDTO response = regionService.createRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ GET ALL REGIONS BY COUNTRY
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<RegionResponseDTO>> getRegionsByCountry(
            @PathVariable Long countryId) {

        List<RegionResponseDTO> regions = regionService.getRegionsByCountry(countryId);
        return ResponseEntity.ok(regions);
    }
    @GetMapping
    public ResponseEntity<List<RegionResponseDTO>> getAllRegions() {
        return ResponseEntity.ok(
                regionRepository.findAll().stream().map(region -> {
                    RegionResponseDTO dto = new RegionResponseDTO();
                    dto.setRegionId(region.getRegionId());
                    dto.setCountryId(region.getCountry().getCountryId());
                    dto.setRegionName(region.getRegionName());
                    dto.setRegionCode(region.getRegionCode());
                    dto.setRegionType(region.getRegionType());
                    return dto;
                }).toList()
        );
    }
}