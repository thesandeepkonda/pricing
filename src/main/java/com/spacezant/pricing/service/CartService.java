package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.cart.CartItemRequest;
import com.spacezant.pricing.dto.cart.CartRequest;
import com.spacezant.pricing.dto.cart.CartResponse;
import com.spacezant.pricing.dto.tax.PricingRequest;
import com.spacezant.pricing.dto.tax.PricingResponse;
import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import com.spacezant.pricing.entity.Variant;
import com.spacezant.pricing.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final PricingService pricingService;
    private final CouponService couponService;
    private final TaxService taxService;
    private final VariantRepository variantRepository;

    public CartResponse calculateCart(CartRequest request) {

        List<PricingResponse> itemResponses = new ArrayList<>();

        double subtotal = 0;
        double totalDiscount = 0;
        double totalAfterDiscount = 0;

        for (CartItemRequest item : request.getItems()) {

            PricingRequest pricingRequest = new PricingRequest();
            pricingRequest.setVariantId(item.getVariantId());
            pricingRequest.setCountryCode(item.getCountryCode());
            pricingRequest.setRegionId(item.getRegionId());
            pricingRequest.setQuantity(item.getQuantity());

            PricingResponse response =
                    pricingService.calculatePriceWithoutCoupon(pricingRequest);

            itemResponses.add(response);

            subtotal += response.getTotalBasePrice();
            totalDiscount += response.getDiscountAmount();
            totalAfterDiscount += response.getPriceAfterDiscount();
        }

        double couponDiscount = 0;

        if (request.getCouponCode() != null) {
            couponDiscount = couponService.applyCoupon(
                    request.getCouponCode(),
                    totalAfterDiscount
            );
        }

        double afterCouponTotal = totalAfterDiscount - couponDiscount;

        double totalTax = 0;

        for (int i = 0; i < itemResponses.size(); i++) {

            PricingResponse response = itemResponses.get(i);
            CartItemRequest item = request.getItems().get(i);

            double ratio = totalAfterDiscount > 0
                    ? response.getPriceAfterDiscount() / totalAfterDiscount
                    : 0;

            double itemPriceAfterCoupon =
                    response.getPriceAfterDiscount() - (couponDiscount * ratio);

            Variant variant = variantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));
            variant.getTaxClassification().getId();

            TaxRequestDTO taxRequest = new TaxRequestDTO();

            taxRequest.setPrice(itemPriceAfterCoupon);

// 🔥 REQUIRED FIELDS (THIS FIXES YOUR ERROR)
            taxRequest.setCountryCode(item.getCountryCode());
            taxRequest.setRegionId(String.valueOf(item.getRegionId()));
            //taxRequest.setZipCode(item.getzipCode()); // ✅ REQUIRED
            taxRequest.setTaxClassificationId(
                    String.valueOf(variant.getTaxClassification().getId())

            );
            taxRequest.setTaxClassificationId(
                    String.valueOf(variant.getTaxClassification().getId())
            );

            taxRequest.setRegionId(String.valueOf(item.getRegionId()));

            TaxResponseDTO taxResponse =
                    taxService.calculateTaxDetails(taxRequest);

            response.setTotalTaxAmount(taxResponse.getTaxAmount());
            response.setFinalPrice(taxResponse.getFinalPrice());

            totalTax += taxResponse.getTaxAmount();
        }

        double grandTotal = afterCouponTotal + totalTax;

        return CartResponse.builder()
                .items(itemResponses)

                .totalBasePrice(subtotal)
                .totalProductDiscount(totalDiscount)

                .couponDiscount(couponDiscount)
                .totalTax(totalTax)
                .grandTotal(grandTotal)
                .build();
    }
}