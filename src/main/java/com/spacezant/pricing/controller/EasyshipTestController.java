package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.service.ImportDutyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class EasyshipTestController {

    private final ImportDutyService importDutyService;

    @PostMapping("/easyship-duty")
    public double calculateDuty(@RequestBody TaxRequestDTO request) {

        return importDutyService.calculateImportDuty(request);
    }
    @PostMapping("/easyship-duty-details")
    public Object calculateDutyDetails(@RequestBody TaxRequestDTO request) {

        double duty = importDutyService.calculateImportDuty(request);

        return Map.of(
                "price", request.getPrice(),
                "duty", duty,
                "finalPrice", request.getPrice() + duty
        );


    }
}