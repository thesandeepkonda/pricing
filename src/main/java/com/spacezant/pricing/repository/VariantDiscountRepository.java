package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.TaxRate;
import com.spacezant.pricing.entity.VariantCountry;
import com.spacezant.pricing.entity.VariantDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VariantDiscountRepository extends JpaRepository<VariantDiscount, Long> {

    List<VariantDiscount> findByVariantCountryVariantCountryId(Long id);

    boolean existsByVariantCountryVariantCountryIdAndCountryDiscountCountryDiscountId(
            Long variantCountryId,
            Long countryDiscountId
    );

    @Query("""
    SELECT vd FROM VariantDiscount vd
    JOIN FETCH vd.countryDiscount cd
    JOIN FETCH cd.discount d
    JOIN vd.variantCountry vc
    WHERE vc.variant.variantId = :variantId
    AND vc.variantCountryCode = :countryCode
    AND vd.status = 'ACTIVE'
    AND cd.status = 'ACTIVE'
    AND d.active = true
""")
    Optional<VariantDiscount> findActiveDiscount(
            @Param("variantId") Long variantId,
            @Param("countryCode") String countryCode
    );
    // 🔥 BEST METHOD
    @Query("""
    SELECT tr FROM TaxRate tr
    LEFT JOIN FETCH tr.components c
    WHERE tr.taxClassification.id = :classificationId
      AND tr.country.countryCode = :countryCode
      AND tr.status = 'ACTIVE'
      AND CURRENT_DATE BETWEEN tr.effectiveFrom 
                           AND COALESCE(tr.effectiveTo, CURRENT_DATE)
""")
    Optional<TaxRate> findCountryTax(
            Long classificationId,
            String countryCode
    );



}