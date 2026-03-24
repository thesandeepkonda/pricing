package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.AddVariantTaxRequest;
import com.spacezant.pricing.dto.CreateTaxRuleRequest;
import com.spacezant.pricing.dto.TaxRuleResponse;
import com.spacezant.pricing.entity.TaxRule;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.repository.TaxRuleRepository;
import com.spacezant.pricing.repository.VariantCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaxAdminService {

    private final TaxRuleRepository taxRuleRepository;
    private final VariantCountryRepository variantCountryRepository;

    public TaxRuleResponse createTaxRule(CreateTaxRuleRequest request) {

        // Optional: prevent duplicate country
        taxRuleRepository.findByCountryCode(request.getCountryCode())
                .ifPresent(t -> {
                    throw new RuntimeException("Tax rule already exists for this country");
                });

        TaxRule taxRule = TaxRule.builder()
                .countryCode(request.getCountryCode())
                .taxRate(request.getTaxRate())
                .taxType(request.getTaxType())
                .isActive(request.getIsActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TaxRule saved = taxRuleRepository.save(taxRule);

        return TaxRuleResponse.builder()
                .id(saved.getId())
                .countryCode(saved.getCountryCode())
                .taxRate(saved.getTaxRate())
                .taxType(saved.getTaxType())
                .isActive(saved.getIsActive())
                .build();
    }
    @Transactional
    public void applyTaxToVariant(AddVariantTaxRequest request) {

        // 1️⃣ Check variant pricing exists
        VariantCountry pricing = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(
                        request.getVariantId(),
                        request.getCountryCode()
                )
                .orElseThrow(() -> new RuntimeException("Variant pricing not found"));

        // 2️⃣ Check tax rule exists for country
        TaxRule taxRule = taxRuleRepository
                .findByCountryCode(request.getCountryCode())
                .orElseThrow(() -> new RuntimeException("Tax rule not found for country"));

        // 3️⃣ Check active
        if (!Boolean.TRUE.equals(taxRule.getIsActive())) {
            throw new RuntimeException("Tax rule is inactive");
        }

        // 4️⃣ Optional: validation log (no DB change needed)
        // You are NOT storing tax in variant → correct design

        pricing.setUpdatedAt(LocalDateTime.now());
        variantCountryRepository.save(pricing);
    }
}
