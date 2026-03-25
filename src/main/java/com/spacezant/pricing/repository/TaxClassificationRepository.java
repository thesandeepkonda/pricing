package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.TaxClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxClassificationRepository extends JpaRepository<TaxClassification, Long> {

    // ✅ Duplicate check per country
    Optional<TaxClassification> findByHsnCodeAndCountryCountryCode(String hsnCode, String countryCode);

    // ✅ Get by country
    List<TaxClassification> findByCountryCountryCode(String countryCode);

    Optional<TaxClassification> findByHsnCode(String hsnCode);
    // 🔍 Search by description (mobile, clothing)
    @Query("""
SELECT tc FROM TaxClassification tc
WHERE LOWER(tc.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
   OR LOWER(tc.hsnCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
   OR LOWER(tc.externalTaxCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<TaxClassification> searchByDescription(@Param("keyword") String keyword);

    // 📌 Get only descriptions
    @Query("""
        SELECT tc.description FROM TaxClassification tc
    """)
    List<String> findAllDescriptions();
}