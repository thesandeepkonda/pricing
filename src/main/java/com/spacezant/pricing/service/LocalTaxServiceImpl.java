package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import com.spacezant.pricing.entity.TaxRate;
import com.spacezant.pricing.entity.TaxRateComponent;
import com.spacezant.pricing.repository.TaxRateRepository;
import com.spacezant.pricing.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("localTaxService")
@RequiredArgsConstructor
public class LocalTaxServiceImpl implements TaxService {

    private final TaxRateRepository taxRateRepository;
    private final CountryRepository countryRepository;

    // ✅ REQUIRED METHOD (fixes your error)
    @Override
    public double calculateTax(String countryCode, Double price) {

        TaxRequestDTO request = new TaxRequestDTO();
        request.setCountryCode(countryCode);
        request.setPrice(price);

        TaxResponseDTO response = calculateTaxDetails(request);
        return response.getTaxAmount();
    }

    @Override
    @Transactional(readOnly = true)
    public TaxResponseDTO calculateTaxDetails(TaxRequestDTO request) {

        Long classificationId = Long.valueOf(request.getTaxClassificationId());
        Long regionId = Long.valueOf(request.getRegionId());

        Long countryId = countryRepository
                .findByCountryCode(request.getCountryCode())
                .orElseThrow(() -> new RuntimeException("Country not found"))
                .getCountryId(); // ✅ FIXED (NOT getId())

        TaxRate taxRate = taxRateRepository.findActiveTax(
                classificationId,
                countryId,
                regionId
        ).orElseGet(() ->
                taxRateRepository.findRegionTax(classificationId, countryId, regionId)
                        .orElseGet(() ->
                                taxRateRepository.findAnyCountryTax(classificationId, countryId)
                                        .orElseThrow(() ->
                                                new RuntimeException("Tax not found for country=" + request.getCountryCode())
                                        )
                        )
        );

        double price = request.getPrice();

        List<TaxRateComponent> components = taxRate.getComponents();

        if (components == null || components.isEmpty()) {
            throw new RuntimeException("No tax components configured");
        }
        if (request.getTaxClassificationId() == null ||
                request.getRegionId() == null) {

            throw new RuntimeException("TaxClassificationId and RegionId are required for local tax");
        }

        double totalTaxPercent = components.stream()
                .mapToDouble(TaxRateComponent::getPercentage)
                .sum();

        double totalTaxAmount = (price * totalTaxPercent) / 100;

        double finalPrice = price + totalTaxAmount;

        return TaxResponseDTO.builder()
                .price(price)
                .totalTaxPercentage(totalTaxPercent)
                .taxAmount(totalTaxAmount)
                .finalPrice(finalPrice)
                .taxType(taxRate.getTaxType())
                .build();
    }
}