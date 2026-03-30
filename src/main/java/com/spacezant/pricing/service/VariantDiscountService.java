package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.discounts.FinalDiscountPriceResponseDTO;
import com.spacezant.pricing.dto.product.PricingInfo;
import com.spacezant.pricing.dto.product.VariantDetailResponseDTO;
import com.spacezant.pricing.enums.DiscountCategoryType;
import com.spacezant.pricing.entity.CountryDiscount;
import com.spacezant.pricing.entity.Discount;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.entity.VariantDiscount;
import com.spacezant.pricing.repository.CountryDiscountRepository;
import com.spacezant.pricing.repository.VariantCountryRepository;
import com.spacezant.pricing.repository.VariantDiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantDiscountService {

    private final VariantCountryRepository variantCountryRepository;
    private final VariantDiscountRepository variantDiscountRepository;
    private final CountryDiscountRepository countryDiscountRepository;


    @Transactional(readOnly = true)
    public VariantDetailResponseDTO getVariantDetails(Long variantId, String countryCode) {

        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                .orElseThrow(() -> new RuntimeException(
                        "No price found for variant " + variantId + " in country " + countryCode));

        Double basePrice = vc.getBasePrice();


        List<VariantDiscount> discounts =
                variantDiscountRepository.findByVariantCountryVariantCountryId(
                        vc.getVariantCountryId()
                );

        LocalDateTime now = LocalDateTime.now();

        List<VariantDiscount> validDiscounts = discounts.stream()
                .filter(vd -> "ACTIVE".equalsIgnoreCase(vd.getStatus()))
                .filter(vd -> vd.getStartDate() == null || !vd.getStartDate().isAfter(now))
                .filter(vd -> vd.getEndDate() == null || !vd.getEndDate().isBefore(now))
                .toList();

        Double maxDiscount = 0.0;
        Discount bestDiscount = null;

        for (VariantDiscount vd : validDiscounts) {

            Discount d = vd.getCountryDiscount().getDiscount();
            Double discountAmount = 0.0;

            if (d.getCategoryType() == DiscountCategoryType.FIXED) {
                discountAmount = d.getDiscountValue();
            }
            else if (d.getCategoryType() == DiscountCategoryType.PERCENTAGE) {

                discountAmount = (basePrice * d.getDiscountValue()) / 100;

                if (d.getMaxDiscount() != null &&
                        discountAmount > d.getMaxDiscount()) {
                    discountAmount = d.getMaxDiscount();
                }
            }

            if (discountAmount > maxDiscount) {
                maxDiscount = discountAmount;
                bestDiscount = d;
            }
        }

        Double finalPrice = basePrice - maxDiscount;

        VariantDetailResponseDTO dto = new VariantDetailResponseDTO();
        dto.setVariantId(variantId);
        dto.setMrp(basePrice);
        dto.setDiscountAmount(maxDiscount);
        dto.setFinalPrice(finalPrice);
        dto.setCurrency(vc.getCurrency());
        dto.setCountryCode(countryCode);

        dto.setDiscountName(
                bestDiscount != null ? bestDiscount.getDiscountName() : "NO DISCOUNT"
        );

        return dto;
    }
    @Transactional(readOnly = true)
    public Map<Long, PricingInfo> getBulkPricing(List<Long> variantIds, String countryCode) {

        Map<Long, PricingInfo> response = new HashMap<>();

        if (variantIds == null || variantIds.isEmpty()) {
            return response;
        }

        for (Long variantId : variantIds) {

            try {
                VariantCountry vc = variantCountryRepository
                        .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                        .orElse(null);

                if (vc == null) continue;

                Double basePrice = vc.getBasePrice();

                List<VariantDiscount> discounts =
                        variantDiscountRepository.findByVariantCountryVariantCountryId(
                                vc.getVariantCountryId()
                        );

                LocalDateTime now = LocalDateTime.now();

                List<VariantDiscount> validDiscounts = discounts.stream()
                        .filter(vd -> "ACTIVE".equalsIgnoreCase(vd.getStatus()))
                        .filter(vd -> vd.getStartDate() == null || !vd.getStartDate().isAfter(now))
                        .filter(vd -> vd.getEndDate() == null || !vd.getEndDate().isBefore(now))
                        .toList();

                Double maxDiscount = 0.0;
                Discount bestDiscount = null;

                List<Long> discountIds = new ArrayList<>();

                for (VariantDiscount vd : validDiscounts) {

                    Discount d = vd.getCountryDiscount().getDiscount();
                    discountIds.add(d.getDiscountId());

                    Double discountAmount = 0.0;

                    if (d.getCategoryType() == DiscountCategoryType.FIXED) {
                        discountAmount = d.getDiscountValue();
                    } else if (d.getCategoryType() == DiscountCategoryType.PERCENTAGE) {

                        discountAmount = (basePrice * d.getDiscountValue()) / 100;

                        if (d.getMaxDiscount() != null &&
                                discountAmount > d.getMaxDiscount()) {
                            discountAmount = d.getMaxDiscount();
                        }
                    }

                    if (discountAmount > maxDiscount) {
                        maxDiscount = discountAmount;
                        bestDiscount = d;
                    }
                }

                double finalPrice = basePrice - maxDiscount;

                // ✅ Build response
                PricingInfo info = new PricingInfo();
                info.setCountryCode(countryCode);
                info.setCurrency(vc.getCurrency());

                info.setBasePrice(basePrice);
                info.setDiscount( maxDiscount);
                info.setFinalPrice(finalPrice);

                info.setDiscountId(discountIds);

                info.setExpoAllowed(vc.getExportAllowed());
                info.setImpoAllowed(vc.getImportAllowed());

                info.setActive("ACTIVE".equalsIgnoreCase(vc.getStatus()));


                response.put(variantId, info);

            } catch (Exception e) {
                log.error("Error processing variantId {}: {}", variantId, e.getMessage());
            }
        }

        return response;
    }
}