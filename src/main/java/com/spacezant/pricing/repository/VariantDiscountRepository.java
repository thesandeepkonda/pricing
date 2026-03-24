package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.entity.VariantDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantDiscountRepository extends JpaRepository<VariantDiscount, Long> {
    // Ensure this also returns Optional

    List<VariantDiscount> findByVariantCountryVariantCountryId(Long id);

    boolean existsByVariantCountryVariantCountryIdAndCountryDiscountCountryDiscountId(Long variantCountryId, Long countryDiscountId);
}