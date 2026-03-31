package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.cart.CartItemRequest;
import com.spacezant.pricing.dto.cart.CartRequest;
import com.spacezant.pricing.dto.cart.CartResponse;
import com.spacezant.pricing.dto.tax.*;
import com.spacezant.pricing.entity.Variant;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.repository.VariantCountryRepository;
import com.spacezant.pricing.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final VariantCountryRepository variantCountryRepository;
    private final VariantRepository variantRepository;
    private final DiscountService discountService;
    private final CouponService couponService;
    private final TaxService taxService;

    public CartResponse calculateCart(CartRequest request) {

        List<Long> variantIds = request.getItems()
                .stream()
                .map(CartItemRequest::getVariantId)
                .toList();

        Map<Long, VariantCountry> vcMap =
                variantCountryRepository
                        .findAllByVariantVariantIdInAndVariantCountryCodeAndStatus(
                                variantIds,
                                request.getCountryCode(),
                                "ACTIVE"
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                vc -> vc.getVariant().getVariantId(),
                                vc -> vc,
                                (a, b) -> a
                        ));

        List<PricingResponse> responses = new ArrayList<>();

        double subtotal = 0;
        double totalDiscount = 0;
        double totalAfterDiscount = 0;

        for (CartItemRequest item : request.getItems()) {

            VariantCountry vc = vcMap.get(item.getVariantId());
            if (vc == null) throw new RuntimeException("Price not found");

            double basePrice = vc.getBasePrice();
            int qty = item.getQuantity();

            double totalBase = basePrice * qty;

            DiscountResult discount = discountService.calculateDiscount(
                    item.getVariantId(),
                    request.getCountryCode(),
                    totalBase,
                    qty
            );

            double discountAmount = discount.getDiscountAmount();
            double afterDiscount = totalBase - discountAmount;

            PricingResponse res = new PricingResponse();
            res.setVariantId(item.getVariantId());
            res.setBasePrice(basePrice);
            res.setQuantity(qty);
            res.setTotalBasePrice(totalBase);
            res.setDiscountAmount(discountAmount);
            res.setPriceAfterDiscount(afterDiscount);

            res.setCurrency(vc.getCurrency());

            responses.add(res);

            subtotal += totalBase;
            totalDiscount += discountAmount;
            totalAfterDiscount += afterDiscount;
        }

        double couponDiscount = 0;

        if (request.getCouponCode() != null) {
            couponDiscount = couponService.applyCoupon(
                    request.getUserId(),
                    request.getCouponCode(),
                    totalAfterDiscount
            );
        }

        double totalTax = 0;

        for (PricingResponse res : responses) {

            double ratio = totalAfterDiscount > 0
                    ? res.getPriceAfterDiscount() / totalAfterDiscount
                    : 0;

            double itemAfterCoupon =
                    res.getPriceAfterDiscount() - (couponDiscount * ratio);

            Variant variant = variantRepository.findById(res.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));

            TaxRequestDTO taxReq = new TaxRequestDTO();
            taxReq.setTaxClassificationId(
                    variant.getTaxClassification().getId()
            );
            taxReq.setCountryCode(request.getCountryCode());
            taxReq.setRegionId(request.getRegionId());
            taxReq.setPrice(itemAfterCoupon);

            TaxResponseDTO taxRes = taxService.calculateTaxDetails(taxReq);

            res.setTotalTaxAmount(taxRes.getTaxAmount());
            res.setFinalPrice(taxRes.getFinalPrice());

            totalTax += taxRes.getTaxAmount();
        }

        double grandTotal = (totalAfterDiscount - couponDiscount) + totalTax;

        return CartResponse.builder()
                .items(responses)
                .totalBasePrice(subtotal)
                .totalProductDiscount(totalDiscount)
                .couponDiscount(couponDiscount)
                .totalTax(totalTax)
                .grandTotal(grandTotal)
                .build();
    }
}