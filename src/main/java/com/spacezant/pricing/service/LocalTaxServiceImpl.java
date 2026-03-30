package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import com.spacezant.pricing.dto.tax.TaxComponentBreakdown;
import com.spacezant.pricing.entity.TaxRate;
import com.spacezant.pricing.entity.TaxRateComponent;
import com.spacezant.pricing.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("localTaxService")
@RequiredArgsConstructor
public class LocalTaxServiceImpl implements TaxService {

    private final TaxRateRepository taxRateRepository;


    // ✅ SIMPLE TAX METHOD (optional)
    @Override
    public double calculateTax(String countryCode, Double price) {
        return 0; // you can implement later
    }


    // ✅ MAIN METHOD (ONLY ONE)
    @Override
    @Transactional(readOnly = true)
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {

        // ✅ 1. Try REGION-specific tax
        Optional<TaxRate> regionTax = taxRateRepository.findActiveTax(
                request.getTaxClassificationId(),
                request.getCountryCode(),
                request.getRegionId()
        );

        TaxRate taxRate;

        if (regionTax.isPresent() && regionTax.get().getRegion() != null) {
            // ✅ Found region-specific tax
            taxRate = regionTax.get();
        } else {
            taxRate = taxRateRepository
                    .findRegionTax(
                            request.getTaxClassificationId(),
                            request.getCountryCode(),
                            request.getRegionId()
                    )
                    .orElseGet(() ->
                            taxRateRepository.findAnyCountryTax(
                                    request.getTaxClassificationId(),
                                    request.getCountryCode()
                            ).orElseThrow(() ->
                                    new RuntimeException("Tax not found for country=" + request.getCountryCode())
                            )
                    );
        }

        double price = request.getPrice();

        List<TaxRateComponent> components = taxRate.getComponents();

        double totalTaxPercent = components.stream()
                .mapToDouble(TaxRateComponent::getPercentage)
                .sum();

        double totalTaxAmount = (price * totalTaxPercent) / 100;

        List<TaxComponentBreakdown> componentBreakdownList =
                components.stream()
                        .map(component -> {
                            double componentAmount =
                                    (price * component.getPercentage()) / 100;

                            return TaxComponentBreakdown.builder()
                                    .componentName(component.getTaxComponentType().name())
                                    .percentage(component.getPercentage())
                                    .amount(componentAmount)
                                    .build();
                        })
                        .toList();

        double finalPrice = price + totalTaxAmount;

        return TaxResponseDTO.builder()
                .price(price)
                .totalTaxPercentage(totalTaxPercent)
                .taxAmount(totalTaxAmount)
                .finalPrice(finalPrice)
                .taxType(taxRate.getTaxType())
                .components(componentBreakdownList)
                .build();
    }
}