package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.CouponRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRuleRepository extends JpaRepository<CouponRule, Long> { // Changed to Long
    List<CouponRule> findByCouponCouponId(Long couponId);

}
