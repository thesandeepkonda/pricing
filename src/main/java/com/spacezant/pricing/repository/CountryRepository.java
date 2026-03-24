package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.CountryDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCountryCode(String countryCode);
}