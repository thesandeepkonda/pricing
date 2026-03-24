package com.spacezant.pricing.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tax_classification")
public class TaxClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hsn_code", nullable = false, length = 10)
    private String hsnCode;

    @Column(length = 255)
    private String description;

    // External provider mapping (Avalara, TaxJar)
    @Column(name = "external_tax_code", length = 50)
    private String externalTaxCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "varienId", nullable = false)
    private Variant variant;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔗 One classification → many tax rates
    @OneToMany(mappedBy = "taxClassification", cascade = CascadeType.ALL)
    private List<TaxRate> taxRates;
}