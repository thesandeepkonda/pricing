package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxClassificationRequest;
import com.spacezant.pricing.dto.tax.TaxClassificationResponse;
import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.TaxClassification;
import com.spacezant.pricing.repository.CountryRepository;
import com.spacezant.pricing.repository.TaxClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxClassificationService {

    private final TaxClassificationRepository repository;
    private final CountryRepository countryRepository; // ✅ FIXED

    // ✅ CREATE
    public TaxClassificationResponse create(TaxClassificationRequest request) {

        // 🔍 Validate duplicate HSN per country
        repository.findByHsnCodeAndCountryCountryCode(
                request.getHsnCode(),
                request.getCountryCode()
        ).ifPresent(tc -> {
            throw new RuntimeException("HSN already exists for this country: " + request.getHsnCode());
        });

        // 🔍 Fetch Country
        Country country = countryRepository
                .findByCountryCode(request.getCountryCode())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        // 🔥 Create Entity
        TaxClassification entity = new TaxClassification();
        entity.setHsnCode(request.getHsnCode());
        entity.setDescription(request.getDescription());
        entity.setExternalTaxCode(request.getExternalTaxCode());
        entity.setCountry(country); // ✅ IMPORTANT
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        TaxClassification saved = repository.save(entity);

        return map(saved);
    }

    // ✅ GET ALL
    public List<TaxClassificationResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // ✅ GET BY COUNTRY
    public List<TaxClassificationResponse> getByCountry(String countryCode) {
        return repository.findByCountryCountryCode(countryCode)
                .stream()
                .map(this::map)
                .toList();
    }

    // ✅ SEARCH
    public List<TaxClassificationResponse> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return repository.searchByDescription(keyword)
                .stream()
                .map(this::map)
                .toList();
    }

    // 🔁 MAPPER
    private TaxClassificationResponse map(TaxClassification entity) {

        return TaxClassificationResponse.builder()
                .id(entity.getId())
                .countryCode(entity.getCountry().getCountryCode()) // ✅ IMPORTANT
                .countryName(entity.getCountry().getName())
                .hsnCode(entity.getHsnCode())
                .description(entity.getDescription())
                .externalTaxCode(entity.getExternalTaxCode())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}