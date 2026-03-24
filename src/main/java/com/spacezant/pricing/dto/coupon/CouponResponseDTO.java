package com.spacezant.pricing.dto.coupon;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponResponseDTO {

    private Long couponId;
    private String couponCode;
    private String couponName;
    private String description;
    private String discountType;
    private Long discountValue;
    private Long maxDiscountAmount;
    private Long minOrderAmount;
    private String currencyCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}