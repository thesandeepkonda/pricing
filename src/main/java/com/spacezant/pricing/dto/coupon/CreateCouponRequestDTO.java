package com.spacezant.pricing.dto.coupon;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCouponRequestDTO {
    private String couponCode;
    private String couponName;
    private String description;
    private String category;
    private String discountType; // "PERCENTAGE" or "FIXED"
    private Long discountValue;
    private Long maxDiscountAmount;
    private Long minOrderAmount;
    private String currencyCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
    private Integer perUserLimit;
    private String status; // "ACTIVE" or "INACTIVE"
}