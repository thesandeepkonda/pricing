package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByCountryCountryId(Long countryId);
    List<Region> findByCountry(Country country);
    Optional<Region> findByRegionCodeAndCountryCountryId(String regionCode, Long countryId);


}