package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.CreateTaxRuleRequest;
import com.spacezant.pricing.dto.TaxRuleResponse;
import com.spacezant.pricing.service.TaxAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
public class TaxAdminController {

    private final TaxAdminService taxAdminService;

    @PostMapping("/rule")
    public TaxRuleResponse createTaxRule(@RequestBody CreateTaxRuleRequest request) {
        return taxAdminService.createTaxRule(request);
    }
}