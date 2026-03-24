package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.DiscountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCategoryRepository extends JpaRepository<DiscountCategory, Long> {

    Optional<DiscountCategory> findByCategoryType(String categoryType);

}
