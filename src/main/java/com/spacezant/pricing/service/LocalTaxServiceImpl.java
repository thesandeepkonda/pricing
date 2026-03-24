package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.TaxRequestDTO;
import com.spacezant.pricing.dto.TaxResponseDTO;
import com.spacezant.pricing.entity.TaxRule;
import com.spacezant.pricing.repository.TaxRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LocalTaxServiceImpl implements TaxService {

    private final TaxRuleRepository taxRuleRepository;

    @Override
    public Long calculateTax(String countryCode, Long price) {

        TaxRule taxRule = taxRuleRepository
                .findByCountry(countryCode)
                .orElseThrow(() -> new RuntimeException("Tax rule not found"));

        Long taxRate = taxRule.getTaxRate(); // 18

        return (price * taxRate) / 100;
    }

    @Override
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {

        TaxRule taxRule = taxRuleRepository
                .findByCountry(request.getCountryCode())
                .orElseThrow(() -> new RuntimeException("Tax rule not found"));

        Long price = request.getPrice();
        Long taxRate = taxRule.getTaxRate();

        Long taxAmount = (price * taxRate) / 100;
        Long finalPrice = price + taxAmount;

        return TaxResponseDTO.builder()
                .price(price)
                .taxRate(taxRate)
                .taxAmount(taxAmount)
                .finalPrice(finalPrice)
                .taxType(taxRule.getTaxType())
                .build();
    }
}