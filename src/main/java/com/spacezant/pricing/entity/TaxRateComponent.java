package com.spacezant.pricing.entity;

import com.spacezant.pricing.enums.TaxComponentType;
import jakarta.persistence.*;


import lombok.*;

@Entity
@Table(name = "tax_rate_component")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRateComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_rate_id", nullable = false)
    private TaxRate taxRate;

    @Enumerated(EnumType.STRING)
    private TaxComponentType taxComponentType;

    private Double percentage;
}