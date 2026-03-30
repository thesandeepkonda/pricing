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
    private final DiscountService discountService; // future use
    private final TaxService taxService;
    private final CouponService couponService;

    public PricingResponse calculatePrice(PricingRequest request) {

        // ✅ 1. Variant
        Variant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        // ✅ 2. Price
        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCodeAndStatus(
                        request.getVariantId(),
                        request.getCountryCode(),
                        "ACTIVE"
                )
                .orElseThrow(() -> new RuntimeException("Price not found"));

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

        // ✅ 4. COUPON
        double couponDiscount = couponService.applyCoupon(
                request.getCouponCode(),
                afterProductDiscount
        );

        double afterCoupon = afterProductDiscount - couponDiscount;

        // ✅ 5. TAX
        TaxRequestDTO taxRequest = new TaxRequestDTO();

        taxRequest.setTaxClassificationId(
                variant.getTaxClassification().getId()
        );
        taxRequest.setCountryCode(request.getCountryCode());
        taxRequest.setRegionId(request.getRegionId());
        taxRequest.setPrice(afterCoupon);

        // 🔥 IMPORTANT (US TAX)
        taxRequest.setZipCode(request.getZipCode());
        taxRequest.setCity(request.getCity());
        taxRequest.setState(request.getState());
        taxRequest.setAddressLine1(request.getAddressLine1());

        TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);

        // ✅ 6. PREPARE BREAKDOWN FIRST
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

        // ✅ 7. FINAL RESPONSE (ONLY ONE RETURN)
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

                .build();
    }
    public PricingResponse calculatePriceWithoutCoupon(PricingRequest request) {

        // ✅ 1. Variant
        Variant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        // ✅ 2. Country Price
        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCodeAndStatus(
                        request.getVariantId(),
                        request.getCountryCode(),
                        "ACTIVE"
                )
                .orElseThrow(() -> new RuntimeException("Price not found"));

        double basePrice = vc.getBasePrice();
        int qty = request.getQuantity();
        double totalBase = basePrice * qty;

        // ✅ 3. PRODUCT DISCOUNT ONLY
        DiscountResult discountResult = discountService.calculateDiscount(
                variant.getVariantId(),
                request.getCountryCode(),
                totalBase,
                qty
        );

        double discountAmount = discountResult.getDiscountAmount();
        double priceAfterDiscount = totalBase - discountAmount;



        TaxRequestDTO taxRequest = new TaxRequestDTO();
        taxRequest.setTaxClassificationId(
                variant.getTaxClassification().getId()
        );
        taxRequest.setCountryCode(request.getCountryCode());
        taxRequest.setRegionId(request.getRegionId());
        taxRequest.setPrice(priceAfterDiscount);


        TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);

        // ✅ 4. RESPONSE (IMPORTANT: include internal fields)
        return PricingResponse.builder()
                .variantId(variant.getVariantId())

                .basePrice(basePrice)
                .quantity(qty)
                .totalBasePrice(totalBase)

                .discountName(discountResult.getDiscountName())
                .discountAmount(discountAmount)

                .priceAfterDiscount(priceAfterDiscount)

                // 🔥 REQUIRED FOR CART (VERY IMPORTANT)
                .taxClassificationId(variant.getTaxClassification().getId())
                .countryCode(request.getCountryCode())
                .regionId(request.getRegionId())

                // initial tax (will be overridden in cart)
                .totalTaxAmount(taxResponse.getTaxAmount())
                .finalPrice(taxResponse.getFinalPrice())

                .build();
    }
}