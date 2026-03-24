package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private Long taxRate;   // 18 = 18%

    private String taxType; // GST / VAT

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}