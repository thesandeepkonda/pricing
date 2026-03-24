package com.spacezant.pricing.repository;

import com.spacezant.pricing.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, String> {

    // ✅ Total usage of a coupon
    long countByCouponCouponId(Long couponId);

    // ✅ Get all usages by user
    List<CouponUsage> findByUserId(String userId);

    // ✅ Count usage of a coupon by a specific user
    int countByCoupon_CouponIdAndUserId(Long couponId, String userId);
}