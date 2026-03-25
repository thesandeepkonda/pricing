package com.spacezant.pricing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    private String countryCode;

    private String name;

    private String currencyCode;


    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "country")
    private List<Region> regions;

    @OneToMany(mappedBy = "country")
    private List<VariantCountry> variantCountries;

    @OneToMany(mappedBy = "country")
    private List<CountryDiscount> countryDiscounts;

    // 🔗 Country → TaxClassifications (NEW ✅)
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<TaxClassification> taxClassifications;

}