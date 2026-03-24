package com.spacezant.pricing.entity;


import com.spacezant.pricing.enums.DiscountCategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "discount")
@Getter
@Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    private String discountName;

    // BUSINESS TYPE
    @Enumerated(EnumType.STRING)
    private DiscountCategoryType categoryType;

    // VALUE (Long instead of Double)
    private Long discountValue;
    private Long maxDiscount;

    private String description;

    private Boolean active;

    // CONDITIONS
    private Long minOrderAmount;
    private Integer minQuantity;

    private Integer priority;
    private Boolean isStackable;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔹 CATEGORY (YOUR EXISTING FLOW - KEEP)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private DiscountCategory category;

    // 🔹 COUNTRY MAPPING (YOUR FLOW - KEEP)
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<CountryDiscount> countryDiscounts;

//    // 🔥 ADD THIS (IMPORTANT FOR YOUR SYSTEM)
//    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
//    private List<VariantDiscount> variantDiscounts;
}


