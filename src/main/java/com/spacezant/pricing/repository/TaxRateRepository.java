package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    @Query("""
SELECT tr FROM TaxRate tr
WHERE tr.taxClassification.id = :classificationId
AND tr.country.countryCode = :countryCode
AND (tr.region.regionId = :regionId OR tr.region IS NULL)
AND tr.status = 'ACTIVE'
AND CURRENT_DATE BETWEEN tr.effectiveFrom
AND COALESCE(tr.effectiveTo, CURRENT_DATE)
""")
    Optional<TaxRate> findActiveTax(
            Long classificationId,
            String countryCode,
            Long regionId
    );
    @Query("""
    SELECT tr FROM TaxRate tr
    LEFT JOIN FETCH tr.components c
    WHERE tr.taxClassification.id = :classificationId
      AND tr.country.countryCode = :countryCode
      AND tr.region.regionId = :regionId
      AND tr.status = 'ACTIVE'
""")
    Optional<TaxRate> findRegionTax(Long classificationId, String countryCode, Long regionId);
    @Query("""
    SELECT tr FROM TaxRate tr
    LEFT JOIN FETCH tr.components c
    WHERE tr.taxClassification.id = :classificationId
      AND tr.country.countryCode = :countryCode
      AND tr.status = 'ACTIVE'
""")
    Optional<TaxRate> findAnyCountryTax(Long classificationId, String countryCode);
}