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
                totalBase
        );

        double productDiscount = discountResult.getDiscountAmount();
        double afterProductDiscount = totalBase - productDiscount;

        // ✅ 4. COUPON DISCOUNT 🔥
        double couponDiscount = couponService.applyCoupon(
                request.getCouponCode(),
                afterProductDiscount
        );

        double afterCoupon = afterProductDiscount - couponDiscount;

        // ✅ 5. TAX (on final discounted price)
        TaxRequestDTO taxRequest = new TaxRequestDTO();
        taxRequest.setTaxClassificationId(
                variant.getTaxClassification().getId()
        );
        taxRequest.setCountryCode(request.getCountryCode());
        taxRequest.setRegionId(request.getRegionId());
        taxRequest.setPrice(afterCoupon);

        TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);

        // ✅ 6. FINAL RESPONSE
        return PricingResponse.builder()
                .variantId(variant.getVariantId())

                .basePrice(basePrice)
                .quantity(qty)
                .totalBasePrice(totalBase)

                // 🔥 Product Discount
                .discountName(discountResult.getDiscountName())
                .discountAmount(productDiscount)

                // 🔥 Coupon
                .couponCode(request.getCouponCode())
                .couponDiscount(couponDiscount)

                // 🔥 After all discounts
                .priceAfterDiscount(afterCoupon)

                // 🔥 Tax
                .totalTaxAmount(taxResponse.getTaxAmount())
                .taxBreakdown(taxResponse.getComponents().stream()
                        .map(c -> TaxBreakdown.builder()
                                .taxName(c.getComponentName())
                                .percentage(c.getPercentage())
                                .amount(c.getAmount())
                                .build())
                        .toList()
                )

                // 🔥 FINAL
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
                totalBase
        );

        double discountAmount = discountResult.getDiscountAmount();
        double priceAfterDiscount = totalBase - discountAmount;

        // ❌ NO COUPON HERE

        // ❌ NO FINAL TAX (cart will recalculate)
        // but we keep initial tax (optional)

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