package com.spacezant.pricing.kafka;

import com.spacezant.pricing.dto.product.*;
import com.spacezant.pricing.entity.*;
import com.spacezant.pricing.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductKafkaConsumer {

    private final VariantRepository variantRepository;
    private final VariantCountryRepository variantCountryRepository;
    private final CountryDiscountRepository countryDiscountRepository;
    private final VariantDiscountRepository variantDiscountRepository;
    private final CountryRepository countryRepository;
    private final DiscountRepository discountRepository;
    private final TaxClassificationRepository taxClassificationRepository;
    //private final PricingEventService pricingEventService;

    @Transactional
    @KafkaListener(topics = "add-product")
    public void consume(ProductCreatedEvent event) {

        log.info("🔥 Received event: {}", event);

        if (event == null || event.getProductData() == null) return;

        List<VariantEvent> variants = event.getProductData().getVariants();
        if (variants == null || variants.isEmpty()) return;

        for (VariantEvent variantEvent : variants) {

            Long variantId = variantEvent.getId();

            if (variantId == null || variantId == 0) {
                variantId = event.getVariantId();
            }
            if (variantId == null) variantId = event.getVariantId();
            if (variantId == null) continue;

            // ✅ SAVE VARIANT
            Variant variant = variantRepository.findById(variantId)
                    .orElse(new Variant());

            variant.setVariantId(variantId);
            variant.setProductId(event.getProductId());

            variant.setSku(
                    variantEvent.getSkuId() != null
                            ? variantEvent.getSkuId()
                            : event.getSkuId()
            );

            variant.setVariantName(variantEvent.getVariantName());
            variant.setStatus(variantEvent.getStatus());
            variant.setUpdatedAt(LocalDateTime.now());

            if (variant.getCreatedAt() == null) {
                variant.setCreatedAt(LocalDateTime.now());
            }

            // 🔥🔥🔥 ADD THIS BLOCK
            for (PricingEvent pricingEvent : variantEvent.getPricing()) {

                // 🔥 SET TAX FROM pricingEvent
                if (pricingEvent.getTaxClassificationId() != null) {

                    TaxClassification tax = taxClassificationRepository
                            .findById(pricingEvent.getTaxClassificationId())
                            .orElseThrow(() -> new RuntimeException("Invalid Tax Classification"));

                    variant.setTaxClassification(tax); // ✅ FIX
                }

                // ✅ SAVE AFTER SETTING TAX
                variantRepository.save(variant);

                // ✅ PRICING LOOP
              //  for (PricingEvent pricingEvent : variantEvent.getPricing()) {

                    Optional<VariantCountry> existing =
                            variantCountryRepository
                                    .findByVariantVariantIdAndVariantCountryCode(
                                            variantId,
                                            pricingEvent.getCountryCode()
                                    );

                    VariantCountry vc = existing.orElse(new VariantCountry());

                    vc.setVariant(variant);
                    vc.setVariantCountryCode(pricingEvent.getCountryCode());
                    vc.setCurrency(pricingEvent.getCurrency());

                    vc.setBasePrice(
                            pricingEvent.getBasePrice() != null
                                    ? pricingEvent.getBasePrice()
                                    : pricingEvent.getFinalPrice() != null
                                    ? pricingEvent.getFinalPrice()
                                    : 0.0
                    );

                    vc.setExportAllowed(pricingEvent.getExpoAllowed());
                    vc.setImportAllowed(pricingEvent.getImpoAllowed());

                    // ✅ HSN
                    if (event.getProductData().getProductInfo() != null) {
                        vc.setHSNno(
                                event.getProductData()
                                        .getProductInfo()
                                        .getHsCode()
                        );
                    }

                    vc.setStatus("ACTIVE");
                    vc.setUpdatedAt(LocalDateTime.now());

                    if (vc.getCreatedAt() == null) {
                        vc.setCreatedAt(LocalDateTime.now());
                    }

                    // 🔥🔥🔥 FIX #1 (MOST IMPORTANT)
                    vc = variantCountryRepository.save(vc);
                    //   pricingEventService.processAndSendPricing(variantId, pricingEvent);

                    // 🔥 FIX #2: handle discount safely
                    if (pricingEvent.getDiscountIds() == null ||
                            pricingEvent.getDiscountIds().isEmpty()) {
                        continue;
                    }

                    for (Long discountId : pricingEvent.getDiscountIds()) {

                        if (discountId == null || discountId <= 0) continue;

                        Optional<Discount> discountOpt = discountRepository.findById(discountId);
                        if (discountOpt.isEmpty()) continue;

                        Discount discount = discountOpt.get();

                        Country country = countryRepository
                                .findByCountryCode(pricingEvent.getCountryCode())
                                .orElseThrow(() -> new RuntimeException("Country not found"));

                        // ✅ SAVE CountryDiscount
                        CountryDiscount cd = new CountryDiscount();
                        cd.setCountry(country);
                        cd.setDiscount(discount);
                        cd.setStatus("ACTIVE");
                        cd.setCreatedAt(LocalDateTime.now());

                        cd = countryDiscountRepository.save(cd);

                        // 🔥 NOW SAFE (vc already saved)
                        VariantDiscount vd = new VariantDiscount();
                        vd.setVariantCountry(vc);
                        vd.setCountryDiscount(cd);
                        vd.setStatus("ACTIVE");
                        vd.setCreatedAt(LocalDateTime.now());

                        variantDiscountRepository.save(vd);
                    }
                }


            }
        }
    }