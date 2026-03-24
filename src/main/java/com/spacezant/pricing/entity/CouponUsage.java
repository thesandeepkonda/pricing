package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_usage")
@Getter
@Setter
public class CouponUsage {

    @Id
    private String usageId;

    private String userId;

    private String orderId;

    private Double discountAmount;

    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}