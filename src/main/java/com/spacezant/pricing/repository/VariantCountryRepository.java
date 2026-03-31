package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.VariantCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface VariantCountryRepository extends JpaRepository<VariantCountry, Long> {

    Optional<VariantCountry> findByVariantVariantIdAndCountryCountryCode(Long variantId, String countryCode);

    Optional<VariantCountry> findByVariantVariantIdAndVariantCountryCode(
            Long variantId,
            String countryCode
    );
    Optional<VariantCountry> findByVariantVariantIdAndVariantCountryCodeAndStatus(
            Long variantId,
            String variantCountryCode,
            String status
    );
    List<VariantCountry> findAllByVariantVariantIdInAndVariantCountryCodeAndStatus(
            List<Long> variantIds,
            String countryCode,
            String status
    );


}