package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    @Query("""
SELECT t FROM TaxRate t
WHERE t.taxClassification.id = :classificationId
AND t.country.id = :countryId
AND t.region.id = :regionId
AND t.status = 'ACTIVE'
""")
    Optional<TaxRate> findActiveTax(
            @Param("classificationId") Long classificationId,
            @Param("countryId") Long countryId,
            @Param("regionId") Long regionId
    );

    @Query("""
SELECT t FROM TaxRate t
WHERE t.taxClassification.id = :classificationId
AND t.country.id = :countryId
AND t.region.id = :regionId
""")
    Optional<TaxRate> findRegionTax(
            @Param("classificationId") Long classificationId,
            @Param("countryId") Long countryId,
            @Param("regionId") Long regionId
    );

    @Query("""
SELECT t FROM TaxRate t
WHERE t.taxClassification.id = :classificationId
AND t.country.id = :countryId
""")
    Optional<TaxRate> findAnyCountryTax(
            @Param("classificationId") Long classificationId,
            @Param("countryId") Long countryId
    );
}