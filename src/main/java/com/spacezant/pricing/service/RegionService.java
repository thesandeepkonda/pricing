package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.RegionRequestDTO;
import com.spacezant.pricing.dto.product.RegionResponseDTO;
import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.Region;
import com.spacezant.pricing.repository.CountryRepository;
import com.spacezant.pricing.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public RegionResponseDTO createRegion(RegionRequestDTO request) {

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        if (regionRepository.findByRegionCodeAndCountryCountryId(
                request.getRegionCode(), request.getCountryId()).isPresent()) {
            throw new RuntimeException("Region code already exists for this country");
        }

        Region region = new Region();
        region.setCountry(country);
        region.setRegionName(request.getRegionName());
        region.setRegionCode(request.getRegionCode());
        region.setRegionType(request.getRegionType());
        region.setStatus(request.getStatus());

        region.setCreatedAt(LocalDateTime.now());
        region.setUpdatedAt(LocalDateTime.now()); // ✅ FIX

        Region saved = regionRepository.save(region);


        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setRegionId(saved.getRegionId());
        dto.setCountryId(saved.getCountry().getCountryId());
        dto.setRegionName(saved.getRegionName());
        dto.setRegionCode(saved.getRegionCode());
        dto.setRegionType(saved.getRegionType());

        return dto;
    }

    public List<RegionResponseDTO> getRegionsByCountry(Long countryId) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        List<Region> regions = regionRepository.findByCountryCountryId(countryId);

        return regions.stream().map(region -> {
            RegionResponseDTO dto = new RegionResponseDTO();
            dto.setRegionId(region.getRegionId());
            dto.setCountryId(region.getCountry().getCountryId());
            dto.setRegionName(region.getRegionName());
            dto.setRegionCode(region.getRegionCode());
            dto.setRegionType(region.getRegionType());
            region.setUpdatedAt(LocalDateTime.now());
            return dto;
        }).toList();
    }
}