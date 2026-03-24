package com.spacezant.pricing.entity;

import com.spacezant.pricing.enums.TaxComponentType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "tax_rate_component")
public class TaxRateComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Parent TaxRate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_rate_id", nullable = false)
    private TaxRate taxRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_component_type", nullable = false)
    private TaxComponentType taxComponentType;

    @Column(nullable = false)
    private Double percentage;
}