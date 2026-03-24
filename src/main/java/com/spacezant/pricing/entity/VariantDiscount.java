package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "variant_discount")
@Getter
@Setter
public class VariantDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantDiscountId;

    private String eventName;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer priority;

    private String description;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔹 Variant + Country combination
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_country_id", nullable = false)
    private VariantCountry variantCountry;

    // 🔥 FIXED: Proper naming
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_discount_id", nullable = false)
    private CountryDiscount countryDiscount;
}