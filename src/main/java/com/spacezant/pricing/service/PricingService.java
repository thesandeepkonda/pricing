package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.*;
import com.spacezant.pricing.entity.Variant;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.repository.VariantCountryRepository;
import com.spacezant.pricing.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final VariantCountryRepository variantCountryRepository;
    private final VariantRepository variantRepository;
    private final DiscountService discountService;
    private final TaxService taxService;
    private final CouponService couponService;

    public PricingResponse calculatePrice(PricingRequest request) {

        // ✅ 1. Fetch Variant
        Variant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException(
                        "Variant not found: " + request.getVariantId()
                ));

        // ✅ 2. Fetch VariantCountry (price + currency)
        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCodeAndStatus(
                        request.getVariantId(),
                        request.getCountryCode(),
                        "ACTIVE"
                )
                .orElseThrow(() -> new RuntimeException(
                        "Price not found for variantId=" + request.getVariantId()
                                + " country=" + request.getCountryCode()
                ));

        double basePrice = vc.getBasePrice();
        int qty = request.getQuantity();
        double totalBase = basePrice * qty;

        // ✅ 3. PRODUCT DISCOUNT
        DiscountResult discountResult = discountService.calculateDiscount(
                variant.getVariantId(),
                request.getCountryCode(),
                totalBase,
                qty
        );

        double productDiscount = discountResult.getDiscountAmount();
        double afterProductDiscount = totalBase - productDiscount;

        // ✅ 4. COUPON (WITH USER ID)
        double couponDiscount = 0;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            couponDiscount = couponService.applyCoupon(
                    request.getUserId(),          // 🔥 IMPORTANT FIX
                    request.getCouponCode(),
                    afterProductDiscount
            );
        }

        double afterCoupon = afterProductDiscount - couponDiscount;

        // ✅ 5. TAX
        TaxRequestDTO taxRequest = new TaxRequestDTO();
        taxRequest.setTaxClassificationId(
                variant.getTaxClassification().getId()
        );
        taxRequest.setCountryCode(request.getCountryCode());
        taxRequest.setRegionId(request.getRegionId());
        taxRequest.setPrice(afterCoupon);

        // Optional (for advanced tax cases like US)
        taxRequest.setZipCode(request.getZipCode());
        taxRequest.setCity(request.getCity());
        taxRequest.setState(request.getState());
        taxRequest.setAddressLine1(request.getAddressLine1());

        TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);

        // ✅ 6. TAX BREAKDOWN
        List<TaxBreakdown> breakdown;

        if (taxResponse.getComponents() != null) {
            breakdown = taxResponse.getComponents().stream()
                    .map(c -> TaxBreakdown.builder()
                            .taxName(c.getComponentName())
                            .percentage(c.getPercentage())
                            .amount(c.getAmount())
                            .build())
                    .toList();
        } else {
            breakdown = List.of(
                    TaxBreakdown.builder()
                            .taxName("SALES_TAX")
                            .percentage(taxResponse.getTotalTaxPercentage())
                            .amount(taxResponse.getTaxAmount())
                            .build()
            );
        }

        // ✅ 7. FINAL RESPONSE (CURRENCY FIXED 🔥)
        return PricingResponse.builder()
                .variantId(variant.getVariantId())

                .basePrice(basePrice)
                .quantity(qty)
                .totalBasePrice(totalBase)

                .discountName(discountResult.getDiscountName())
                .discountAmount(productDiscount)

                .couponCode(request.getCouponCode())
                .couponDiscount(couponDiscount)

                .priceAfterDiscount(afterCoupon)

                .totalTaxAmount(taxResponse.getTaxAmount())
                .taxBreakdown(breakdown)

                .finalPrice(taxResponse.getFinalPrice())

                .currency(vc.getCurrency()) // 🔥 CRITICAL FIX

                .build();
    }
}