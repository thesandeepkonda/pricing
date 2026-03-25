package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.cart.CartItemRequest;
import com.spacezant.pricing.dto.cart.CartRequest;
import com.spacezant.pricing.dto.cart.CartResponse;
import com.spacezant.pricing.dto.tax.PricingRequest;
import com.spacezant.pricing.dto.tax.PricingResponse;
import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
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

        double totalBase = 0;
        double totalDiscount = 0;
        double totalAfterDiscount = 0;

        // ✅ 1. Calculate each item WITHOUT coupon
        for (CartItemRequest item : request.getItems()) {

            PricingRequest pricingRequest = new PricingRequest();
            pricingRequest.setVariantId(item.getVariantId());
            pricingRequest.setCountryCode(item.getCountryCode());
            pricingRequest.setRegionId(item.getRegionId());
            pricingRequest.setQuantity(item.getQuantity());

            // ❗ DO NOT pass coupon here
            PricingResponse response = pricingService.calculatePriceWithoutCoupon(pricingRequest);

            itemResponses.add(response);

            totalBase += response.getTotalBasePrice();
            totalDiscount += response.getDiscountAmount();
            totalAfterDiscount += response.getPriceAfterDiscount();
        }

        // ✅ 2. Apply COUPON ON CART TOTAL
        double couponDiscount = couponService.applyCoupon(
                request.getCouponCode(),
                totalAfterDiscount
        );

        double afterCouponTotal = totalAfterDiscount - couponDiscount;

        // ✅ 3. Recalculate TAX per item (proportional distribution)

        double totalTax = 0;

        for (int i = 0; i < itemResponses.size(); i++) {

            PricingResponse response = itemResponses.get(i);
            CartItemRequest item = request.getItems().get(i); // ✅ ORIGINAL DATA

            double ratio = response.getPriceAfterDiscount() / totalAfterDiscount;

            double itemPriceAfterCoupon =
                    response.getPriceAfterDiscount() - (couponDiscount * ratio);

            // ✅ TAX REQUEST (use original item data)
            TaxRequestDTO taxRequest = new TaxRequestDTO();
            taxRequest.setTaxClassificationId(
                    variantRepository.findById(item.getVariantId())
                            .orElseThrow().getTaxClassification().getId()
            );
            taxRequest.setCountryCode(item.getCountryCode());
            taxRequest.setRegionId(item.getRegionId());
            taxRequest.setPrice(itemPriceAfterCoupon);

            TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);

            response.setTotalTaxAmount(taxResponse.getTaxAmount());
            response.setFinalPrice(taxResponse.getFinalPrice());

            totalTax += taxResponse.getTaxAmount();

           // TaxRequestDTO taxRequest = new TaxRequestDTO();
            taxRequest.setTaxClassificationId(response.getTaxClassificationId());
            taxRequest.setCountryCode(response.getCountryCode());
            taxRequest.setRegionId(response.getRegionId());
            taxRequest.setPrice(itemPriceAfterCoupon);

         //   TaxResponseDTO taxResponse = taxService.calculateTaxDetails(taxRequest);
        }


        double grandTotal = afterCouponTotal + totalTax;

        return CartResponse.builder()
                .items(itemResponses)
                .totalBasePrice(totalBase)
                .totalProductDiscount(totalDiscount)
                .couponDiscount(couponDiscount)
                .totalTax(totalTax)
                .grandTotal(grandTotal)
                .build();
    }
}