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
    public FinalDiscountPriceResponseDTO getFinalPrice(Long variantId, String countryCode) {

        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                .orElseThrow(() -> new RuntimeException(
                        "No price found for Variant " + variantId + " in " + countryCode));

        Double basePrice = vc.getBasePrice();

        // ✅ FIX: Use Long (NOT Double)
        Double basePrice1 = vc.getBasePrice();

        // ✅ 2. Get all discounts
        List<VariantDiscount> discounts =
                variantDiscountRepository.findByVariantCountryVariantCountryId(
                        vc.getVariantCountryId()
                );

        LocalDateTime now = LocalDateTime.now();

        // ✅ 3. Filter valid discounts
        List<VariantDiscount> validDiscounts = discounts.stream()
                .filter(vd -> "ACTIVE".equalsIgnoreCase(vd.getStatus()))
                .filter(vd -> vd.getStartDate() == null || !vd.getStartDate().isAfter(now))
                .filter(vd -> vd.getEndDate() == null || !vd.getEndDate().isBefore(now))
                .toList();

        // ✅ 4. No discount case
        if (validDiscounts.isEmpty()) {
            return buildDTO(variantId, basePrice, basePrice, "NONE", 0.0, vc.getCurrency());
        }

        Double maxDiscountAmount = 0.0;
        Discount bestDiscount = null;

        // ✅ 5. Apply BEST discount
        for (VariantDiscount vd : validDiscounts) {

            CountryDiscount cd = vd.getCountryDiscount();
            Discount d = cd.getDiscount();

            Double value = d.getDiscountValue();
            Double discountAmount = (double) 0;

            // ✅ ENUM SAFE
            if (d.getCategoryType() == DiscountCategoryType.FIXED) {
                discountAmount = value;
            }

            else if (d.getCategoryType() == DiscountCategoryType.PERCENTAGE) {

                // ✅ PURE LONG calculation
                discountAmount = (basePrice * value) / 100;

                if (d.getMaxDiscount() != null && discountAmount > d.getMaxDiscount()) {
                    discountAmount = d.getMaxDiscount();
                }
            }

            if (discountAmount > maxDiscountAmount) {
                maxDiscountAmount = discountAmount;
                bestDiscount = d;
            }
        }

        Double finalPrice = basePrice - maxDiscountAmount;

        return buildDTO(
                variantId,
                basePrice,
                finalPrice,
                bestDiscount != null ? bestDiscount.getCategoryType().name() : "NONE",
                maxDiscountAmount,
                vc.getCurrency()
        );
    }

    @Transactional
    public void assignDiscountToVariant(Long variantId, String countryCode, Long countryDiscountId) {
        log.info("variantId: {}, countryCode: {}", variantId, countryCode);
        VariantCountry vc = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                .orElseThrow(() -> new RuntimeException(
                        "No price found for variant " + variantId + " in country " + countryCode));

        CountryDiscount countryDiscount = countryDiscountRepository
                .findById(countryDiscountId)
                .orElseThrow(() -> new RuntimeException("CountryDiscount not found"));

        boolean exists = variantDiscountRepository
                .existsByVariantCountryVariantCountryIdAndCountryDiscountCountryDiscountId(
                        vc.getVariantCountryId(),
                        countryDiscountId
                );

        if (exists) {
            throw new RuntimeException("Discount already assigned");
        }

        VariantDiscount vd = new VariantDiscount();
        vd.setVariantCountry(vc);
        vd.setCountryDiscount(countryDiscount);
        vd.setCreatedAt(LocalDateTime.now());
        vd.setStatus("ACTIVE");

        variantDiscountRepository.save(vd);
    }

    private FinalDiscountPriceResponseDTO buildDTO(
            Long variantId,
            Double basePrice,
            Double finalPrice,
            String type,
            Double discountValue,
            String currency) {

        FinalDiscountPriceResponseDTO dto = new FinalDiscountPriceResponseDTO();
        dto.setVariantId(variantId);
        dto.setBasePrice(basePrice);
        dto.setFinalPrice(finalPrice);
        dto.setDiscountType(type);
        dto.setDiscountValue(discountValue);
        dto.setCurrency(currency);
        return dto;
    }
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

                info.setBasePrice(basePrice.doubleValue());
                info.setDiscount((double) maxDiscount);
                info.setFinalPrice((double) finalPrice);

                info.setDiscountId(discountIds);

                info.setExpoAllowed(vc.getExportAllowed());
                info.setImpoAllowed(vc.getImportAllowed());

                info.setActive("ACTIVE".equalsIgnoreCase(vc.getStatus()));

                // optional range
                info.setStartFrom(0);
                info.setEndTo((long) finalPrice);

                response.put(variantId, info);

            } catch (Exception e) {
                log.error("Error processing variantId {}: {}", variantId, e.getMessage());
            }
        }

        return response;
    }
}