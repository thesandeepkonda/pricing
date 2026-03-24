package com.spacezant.pricing.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tax_rate")
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 HSN mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_classification_id", nullable = false)
    private TaxClassification taxClassification;

    // 🔗 Country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // 🔗 Region (nullable → country-level tax)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    // 🔗 Rule (INTRA / INTER)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_rule_id", nullable = false)
    private TaxRule taxRule;

    @Column(name = "tax_type", length = 50)
    private String taxType; // GST / VAT

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(length = 20)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔗 One TaxRate → many components
    @OneToMany(mappedBy = "taxRate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaxRateComponent> components;
}