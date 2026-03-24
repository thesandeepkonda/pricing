package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.Country;
import com.spacezant.pricing.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByCountryCountryId(Long countryId);
    List<Region> findByCountry(Country country);

}