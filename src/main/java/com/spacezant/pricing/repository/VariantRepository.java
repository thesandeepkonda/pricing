package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.Variant;
import com.spacezant.pricing.entity.VariantDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    Optional<Variant> findBySku(String sku);

    List<Variant> findByProductId(Long productId);

    // ❌ REMOVE THIS
    // List<VariantDiscount> findCategoryIdByVariantId(Long id);
}