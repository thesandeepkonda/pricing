package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "variant_country")
@Getter
@Setter
public class VariantCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantCountryId;

    private String HSNno;

    private Double basePrice;

    private String currency;

    private Boolean exportAllowed;

    private Boolean importAllowed;

    @Column(name = "variant_country_code")
    private String variantCountryCode;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "variantCountry")
    private List<VariantDiscount> variantDiscounts;

}