package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.product.*;
import com.spacezant.pricing.entity.Variant;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.repository.VariantCountryRepository;
import com.spacezant.pricing.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantCountryService {

    private final VariantCountryRepository repository;
    private final VariantRepository variantRepository;
    private final VariantCountryRepository variantCountryRepository;
    private final TaxService taxService;
    private final DiscountService discountService;

    @Transactional
    public void createProduct(ProductCreateRequest request) {

        if (request.getVariants() == null) return;

        for (VariantEvent variantEvent : request.getVariants()) {

            // ✅ VALIDATION
            if (variantEvent.getId() == null) {
                throw new RuntimeException("variantId is required");
            }

            // ✅ SAVE VARIANT
            Variant variant = new Variant();
            variant.setVariantId(variantEvent.getId());
            variant.setProductId(request.getProduct().getId());
            variant.setSku(variantEvent.getSkuId());
            variant.setStatus(variantEvent.getStatus());
            variant.setCreatedAt(LocalDateTime.now());

            Variant savedVariant = variantRepository.save(variant);


            // ✅ SAVE PRICING
            if (variantEvent.getPricing() != null) {
                for (PricingEvent pricing : variantEvent.getPricing()) {

                    VariantCountry vc = new VariantCountry();
                    vc.setVariant(savedVariant);
                    vc.setVariantCountryCode(pricing.getCountryCode());
                    vc.setCurrency(pricing.getCurrency());
                    vc.setBasePrice(pricing.getMrp());
                    vc.setStatus("ACTIVE");
                    vc.setCreatedAt(LocalDateTime.now());

                    variantCountryRepository.save(vc);
                }
            }
        }
    }

    @Transactional
    public ProductCreateResponse createProductResponse(ProductCreateRequest request) {

        ProductCreateResponse response = new ProductCreateResponse();
        response.setProductId(request.getProduct().getId());

        List<VariantResponse> variantResponses = new ArrayList<>();

        for (VariantEvent variantEvent : request.getVariants()) {

            // ✅ SAVE VARIANT
            Variant variant = new Variant();
            variant.setVariantId(variantEvent.getId()); // IMPORTANT
            variant.setProductId(request.getProduct().getId());
            variant.setSku(variantEvent.getSkuId());
            variant.setStatus(variantEvent.getStatus());
            variant.setCreatedAt(LocalDateTime.now());

            Variant savedVariant = variantRepository.save(variant);

            // ✅ RESPONSE BUILD
            VariantResponse variantResponse = new VariantResponse();
            variantResponse.setVariantId(savedVariant.getVariantId());
            variantResponse.setSku(savedVariant.getSku());

            List<PricingResponse> pricingResponses = new ArrayList<>();

            // ✅ SAVE PRICING
            if (variantEvent.getPricing() != null) {
                for (PricingEvent pricingEvent : variantEvent.getPricing()) {

                    VariantCountry pricing = new VariantCountry();
                    pricing.setVariant(savedVariant);
                    pricing.setVariantCountryCode(pricingEvent.getCountryCode());
                    pricing.setCurrency(pricingEvent.getCurrency());
                    pricing.setBasePrice(pricingEvent.getMrp());
                    pricing.setStatus("ACTIVE");

                    VariantCountry savedPricing = variantCountryRepository.save(pricing);

                    // ✅ RESPONSE DTO
                    PricingResponse pr = new PricingResponse();
                    pr.setCountryCode(savedPricing.getVariantCountryCode());
                    pr.setCurrency(savedPricing.getCurrency());
                    pr.setBasePrice(savedPricing.getBasePrice());
                    pr.setStatus(savedPricing.getStatus());

                    pricingResponses.add(pr);
                }
            }

            variantResponse.setPricing(pricingResponses);
            variantResponses.add(variantResponse);
        }

        response.setVariants(variantResponses);

        return response;
    }

    public PricingDetailResponse getPricingDetails(Long variantId, String countryCode) {

        // 1️⃣ Get pricing
        VariantCountry variantCountry = variantCountryRepository
                .findByVariantVariantIdAndVariantCountryCode(variantId, countryCode)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // ✅ FIX: correct way
        Long productId = variantCountry.getVariant().getProductId();

        // 2️⃣ Discount
        Double discount = discountService.getBestCategoryDiscountByProduct(
                productId,
                variantCountry.getBasePrice()
        );

        if (discount == null) discount = (double) 0L;

        double afterDiscount = variantCountry.getBasePrice() - discount;

        // 3️⃣ Tax
        Double taxAmountLong = taxService.calculateTax(
                countryCode,
                (double) Math.round(afterDiscount)
        );

        double tax = taxAmountLong != null ? taxAmountLong : 0;

        double finalPrice = afterDiscount + tax;

        return PricingDetailResponse.builder()
                .variantId(variantId)
                .countryCode(countryCode)
                .currency(variantCountry.getCurrency()) // ✅ FIXED
                .basePrice(variantCountry.getBasePrice())
                .discount(discount)
                .priceAfterDiscount(afterDiscount)
                .tax(tax)
                .finalPrice((Double) finalPrice)
                .build();
    }
}

