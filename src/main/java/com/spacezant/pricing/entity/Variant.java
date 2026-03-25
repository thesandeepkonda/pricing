package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "variant")
@Getter
@Setter
public class Variant {

    @Id
    @Column(nullable = false)
    private Long variantId;

    private Long productId;

    private String sku;

    private String variantName;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // 🔗 Tax mapping (VERY IMPORTANT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_classification_id")
    private TaxClassification taxClassification;
}
