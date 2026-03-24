package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.discounts.CountryDiscountRequestDTO;
import com.spacezant.pricing.dto.discounts.CountryDiscountResponseDTO;
import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.CountryDiscount;
import com.spacezant.pricing.entity.Discount;
import com.spacezant.pricing.repository.CountryDiscountRepository;
import com.spacezant.pricing.repository.CountryRepository;
import com.spacezant.pricing.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryDiscountService {

    private final CountryDiscountRepository repo;
    private final DiscountRepository discountRepo;
    private final CountryRepository countryRepo;

    public CountryDiscountResponseDTO create(CountryDiscountRequestDTO dto) {

        CountryDiscount cd = new CountryDiscount();

        Discount discount = discountRepo.findById(dto.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        Country country = countryRepo.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        cd.setDiscount(discount);
        cd.setCountry(country);
        cd.setStatus(dto.getStatus());
        cd.setStartDate(dto.getStartDate());
        cd.setEndDate(dto.getEndDate());

        repo.save(cd);

        return map(cd);
    }

    public CountryDiscountResponseDTO get(Long id) {
        return map(repo.findById(id).orElseThrow());
    }

    public List<CountryDiscountResponseDTO> getAll() {
        return repo.findAll().stream().map(this::map).toList();
    }

    private CountryDiscountResponseDTO map(CountryDiscount cd) {
        CountryDiscountResponseDTO dto = new CountryDiscountResponseDTO();
        dto.setCountryDiscountId(cd.getCountryDiscountId());
        dto.setCountryId(cd.getCountry().getCountryId());
        dto.setCountryCode(cd.getCountry().getCountryCode());
        dto.setDiscountId(cd.getDiscount().getDiscountId());
        return dto;
    }
}