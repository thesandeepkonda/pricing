package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tax_classification")
@Getter
@Setter
public class TaxClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hsn_code", nullable = false, length = 10, unique = true)
    private String hsnCode;

    @Column(length = 255)
    private String description;

    @Column(name = "external_tax_code", length = 50)
    private String externalTaxCode;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // 🔗 One HSN → Many TaxRates
    @OneToMany(mappedBy = "taxClassification", fetch = FetchType.LAZY)
    private List<TaxRate> taxRates;

}