package com.spacezant.pricing.service;


import com.spacezant.pricing.dto.product.PricingDetailResponse;
import com.spacezant.pricing.entity.VariantCountry;

import com.spacezant.pricing.repository.VariantCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final VariantCountryRepository variantCountryRepository;
    private final DiscountService discountService;
    private final TaxService taxService;

    public PricingDetailResponse getPricingDetails(Long variantId, String countryCode) {

        VariantCountry pricing = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                .orElseThrow(() -> new RuntimeException("Pricing not found"));

        Double basePrice = pricing.getBasePrice();

        // ✅ FIXED
        Double discount = discountService.getBestCategoryDiscount(
                variantId,
                basePrice
        );

        double afterDiscount = basePrice - discount;

        Double taxAmountLong = taxService.calculateTax(
                countryCode,
                (double) Math.round(afterDiscount)
        );

        double tax = taxAmountLong.doubleValue();

        double finalPrice = afterDiscount + tax;

        return PricingDetailResponse.builder()
                .variantId(variantId)
                .countryCode(countryCode)
                .currency(pricing.getCurrency())
                .basePrice(basePrice)
                .discount(discount)
                .priceAfterDiscount(afterDiscount)
                .tax(tax)
                .finalPrice((Double) finalPrice)
                .build();
    }
}