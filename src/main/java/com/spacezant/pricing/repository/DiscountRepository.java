package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByActiveTrue();

    List<Discount> findByCategoryCategoryId(Long categoryId);
    List<Discount> findByCategoryCategoryIdAndActiveTrue(Long categoryId);


}