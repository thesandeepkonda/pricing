package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.discounts.*;
import com.spacezant.pricing.entity.Discount;
import com.spacezant.pricing.entity.DiscountCategory;
import com.spacezant.pricing.enums.DiscountCategoryType;
import com.spacezant.pricing.repository.DiscountCategoryRepository;
import com.spacezant.pricing.repository.DiscountRepository;
import com.spacezant.pricing.repository.VariantCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountCategoryRepository categoryRepository;
    private final VariantCountryRepository variantCountryRepository;

    // ================= CREATE =================

    public DiscountResponseDTO createDiscount(DiscountRequestDTO dto) {

        Discount discount = new Discount();

        discount.setDiscountName(dto.getDiscountName());
        discount.setCategoryType(DiscountCategoryType.valueOf(dto.getCategoryType()));
        discount.setDiscountValue(Double.valueOf(dto.getDiscountValue()));
        discount.setMaxDiscount(dto.getMaxDiscount());
        discount.setDescription(dto.getDescription());
        discount.setActive(dto.getActive());
        discount.setPriority(dto.getPriority());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        discount.setMinQuantity(dto.getMinQuantity());
        discount.setMinOrderAmount(dto.getMinOrderAmount());
        discount.setIsStackable(dto.getIsStackable());
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());


        DiscountCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        discount.setCategory(category);

        Discount saved = discountRepository.save(discount);

        return mapToResponse(saved);
    }

    // ================= GET =================

    public DiscountResponseDTO getDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return mapToResponse(discount);
    }

    public List<DiscountResponseDTO> getAllDiscounts() {
        return discountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= UPDATE =================

    public DiscountResponseDTO updateDiscount(Long id, DiscountRequestDTO dto) {

        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        discount.setDiscountName(dto.getDiscountName());
        discount.setDiscountValue(Double.valueOf(dto.getDiscountValue()));
        discount.setMaxDiscount(dto.getMaxDiscount());
        discount.setMinOrderAmount(dto.getMinOrderAmount());
        discount.setIsStackable(dto.getIsStackable());
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(discountRepository.save(discount));
    }

    // ================= DELETE =================

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    // ================= CORE ENGINE =================

    /**
     * 🔥 MAIN DISCOUNT ENGINE (USE THIS EVERYWHERE)
     */
    public Double calculateBestDiscount(Long categoryId, Double price, int quantity) {

        List<Discount> discounts = discountRepository.findByCategoryCategoryId(categoryId);

        if (discounts == null || discounts.isEmpty()) {
            return (double) 0;
        }

        // Optional: priority sorting
        discounts.sort(Comparator.comparing(Discount::getPriority, Comparator.nullsLast(Integer::compareTo)));

        Double maxDiscount = (double) 0;
        LocalDateTime now = LocalDateTime.now();

        for (Discount d : discounts) {

            // ACTIVE CHECK
            if (d.getActive() == null || !d.getActive()) continue;

            // DATE CHECK
            if (d.getStartDate() != null && d.getStartDate().isAfter(now)) continue;
            if (d.getEndDate() != null && d.getEndDate().isBefore(now)) continue;

            Double discountAmount = (double) 0;

            switch (d.getCategoryType()) {

                case FIXED:
                    discountAmount = d.getDiscountValue();
                    break;

                case PERCENTAGE:
                    discountAmount = (price * d.getDiscountValue()) / 100;

                    if (d.getMaxDiscount() != null &&
                            discountAmount > d.getMaxDiscount()) {
                        discountAmount = d.getMaxDiscount();
                    }
                    break;

                case BULK:
                    if (d.getMinQuantity() != null && quantity >= d.getMinQuantity()) {
                        discountAmount = (price * d.getDiscountValue()) / 100;
                    }
                    break;

                case BUY_X_GET_Y:
                    if (d.getMinQuantity() != null && quantity >= d.getMinQuantity()) {
                        int freeItems = quantity / d.getMinQuantity();
                        discountAmount = freeItems * (price / quantity);
                    }
                    break;

                case BUNDLE:
                    discountAmount = d.getDiscountValue();
                    break;

                case FREE_SHIPPING:
                    // handled separately
                    continue;

                default:
                    continue;
            }

            if (discountAmount > maxDiscount) {
                maxDiscount = discountAmount;
            }
        }

        return maxDiscount;
    }

    /**
     * 🔹 For product-based calls (your current design)
     */
    public Double getBestCategoryDiscount(Long productId, Double price) {
        return calculateBestDiscount(productId, price, 1);
    }

    // ================= MAPPER =================

    private DiscountResponseDTO mapToResponse(Discount d) {

        DiscountResponseDTO dto = new DiscountResponseDTO();

        dto.setDiscountId(d.getDiscountId());
        dto.setDiscountName(d.getDiscountName());
        dto.setCategoryType(d.getCategoryType().name());
        dto.setDiscountValue(d.getDiscountValue());
        dto.setMaxDiscount(d.getMaxDiscount());

        dto.setMinOrderAmount(
                d.getMinOrderAmount() != null ? d.getMinOrderAmount() : 0L
        );

        dto.setDescription(d.getDescription());
        dto.setActive(d.getActive());
        dto.setPriority(d.getPriority());
        dto.setStartDate(d.getStartDate());
        dto.setEndDate(d.getEndDate());

        return dto;
    }


    public Double getBestCategoryDiscountByProduct(Long productId, Double basePrice) {

        Long categoryId = productId; // your current design

        List<Discount> discounts =
                discountRepository.findByCategoryCategoryIdAndActiveTrue(categoryId);
        if (productId == null || basePrice == null) return (double) 0L;


        Double bestDiscount = (double) 0;

        for (Discount discount : discounts) {

            Double calculatedDiscount = (double) 0;

            // ✅ ENUM FIX
            if (discount.getCategoryType() == DiscountCategoryType.PERCENTAGE) {

                calculatedDiscount = (Double) ((basePrice * discount.getDiscountValue()) / 100);

                if (discount.getMaxDiscount() != null &&
                        calculatedDiscount > discount.getMaxDiscount()) {
                    calculatedDiscount = discount.getMaxDiscount();
                }

            } else if (discount.getCategoryType() == DiscountCategoryType.FIXED) {

                calculatedDiscount = discount.getDiscountValue();
            }

            if (calculatedDiscount > bestDiscount) {
                bestDiscount = calculatedDiscount;
            }
        }

        return bestDiscount;
    }
}