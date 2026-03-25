package com.spacezant.pricing.kafka;


import com.spacezant.pricing.dto.product.PricingEvent;
import com.spacezant.pricing.dto.product.VariantPricingEvent;
import com.spacezant.pricing.entity.Discount;
import com.spacezant.pricing.enums.DiscountCategoryType;
import com.spacezant.pricing.repository.DiscountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PricingEventService {
//
//    private final DiscountRepository discountRepository;
//   private final PricingKafkaProducer pricingKafkaProducer;
//
//    public void processAndSendPricing(Long variantId, PricingEvent pricingEvent) {
//
//        if (variantId == null || pricingEvent == null) {
//            log.warn("Invalid data for pricing");
//            return;
//        }
//
//        Double mrp = (double) (pricingEvent.getMrp() != null
//                        ? pricingEvent.getMrp().longValue()
//                        : 0L);
//
//        Double maxDiscount = 0.0;
//        String discountName = "NO DISCOUNT";
//
//        if (pricingEvent.getDiscountIds() != null) {
//
//            for (Long discountId : pricingEvent.getDiscountIds()) {
//
//                if (discountId == null || discountId <= 0) continue;
//
//                Discount discount = discountRepository.findById(discountId)
//                        .orElse(null);
//
//                if (discount == null || Boolean.FALSE.equals(discount.getActive())) {
//                    continue;
//                }
//
//                Double calculatedDiscount = 0.0;
//
//                if (discount.getCategoryType() == DiscountCategoryType.FIXED) {
//
//                    calculatedDiscount = discount.getDiscountValue();
//
//                } else if (discount.getCategoryType() == DiscountCategoryType.PERCENTAGE) {
//
//                    calculatedDiscount = (mrp * discount.getDiscountValue()) / 100;
//
//                    if (discount.getMaxDiscount() != null &&
//                            calculatedDiscount > discount.getMaxDiscount()) {
//
//                        calculatedDiscount = discount.getMaxDiscount();
//                    }
//                }
//
//                // ✅ PICK BEST DISCOUNT
//                if (calculatedDiscount > maxDiscount) {
//                    maxDiscount = calculatedDiscount;
//                    discountName = discount.getDiscountName();
//                }
//            }
//        }
//
//        Double finalAmount = mrp - maxDiscount;
//        if (finalAmount < 0) finalAmount = (double) 0;
//
//        VariantPricingEvent event = new VariantPricingEvent();
//
//        event.setVariantId(variantId);
//        event.setMrp(mrp);
//        event.setDiscountAmount(maxDiscount);
//        event.setFinalAmount(finalAmount);
//        event.setCurrency(pricingEvent.getCurrency());
//        event.setCountryCode(pricingEvent.getCountryCode());
//
//        // 👉 Optional (VERY USEFUL)
//        event.setDiscountName(discountName);
//
//      //  pricingKafkaProducer.sendPricing(event);
//
//        log.info("✅ Pricing event sent: {}", event);
//    }
//}