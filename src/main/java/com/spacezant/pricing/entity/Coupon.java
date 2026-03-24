package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupon")
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String couponCode;

    private String couponName;

    private String description;

    private String discountType; // FIXED or PERCENTAGE

    private Long discountValue;

    private Long maxDiscountAmount;

    private String currencyCode;

    private Long minOrderAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer usageLimit;

    private Integer perUserLimit;

    private Integer totalUsed;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponRule> rules;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponUsage> usages;
}