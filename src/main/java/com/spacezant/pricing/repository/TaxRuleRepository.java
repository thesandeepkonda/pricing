package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {

    @Query("""
        SELECT t FROM TaxRule t
        WHERE t.countryCode = :countryCode
        AND t.isActive = true
    """)
    Optional<TaxRule> findByCountry(String countryCode);
    Optional<TaxRule> findByCountryCode(String countryCode);
}