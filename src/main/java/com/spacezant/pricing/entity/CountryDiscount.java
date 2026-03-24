package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "country_discount")
@Getter
@Setter
public class CountryDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryDiscountId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String description;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔹 Country mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // 🔹 Parent Discount
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    // 🔥 FIXED: mappedBy must match field name in VariantDiscount
    @OneToMany(mappedBy = "countryDiscount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantDiscount> variantDiscounts;
}

