package com.spacezant.pricing.service;


import com.spacezant.pricing.dto.CountryRequestDTO;
import com.spacezant.pricing.dto.CountryResponseDTO;
import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;

    public CountryResponseDTO createCountry(CountryRequestDTO request) {

        Country country = new Country();
        country.setCountryCode(request.getCountryCode());
        country.setName(request.getName());
        country.setCurrencyCode(request.getCurrencyCode());
        country.setStatus(request.getStatus());
        country.setCreatedAt(LocalDateTime.now());

        Country saved = repository.save(country);

        CountryResponseDTO dto = new CountryResponseDTO();
        dto.setCountryId(saved.getCountryId());
        dto.setCountryCode(saved.getCountryCode());
        dto.setName(saved.getName());
        dto.setCurrencyCode(saved.getCurrencyCode());

        return dto;
    }

    public List<CountryResponseDTO> getAllCountries() {

        return repository.findAll().stream().map(c -> {
            CountryResponseDTO dto = new CountryResponseDTO();
            dto.setCountryId(c.getCountryId());
            dto.setCountryCode(c.getCountryCode());
            dto.setName(c.getName());
            dto.setCurrencyCode(c.getCurrencyCode());
            return dto;
        }).toList();
    }
}
