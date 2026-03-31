package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AvalaraService {

    private final WebClient webClient;

    public TaxResponseDTO calculateTax(TaxRequestDTO request) {

        if (request.getZipCode() == null) {
            throw new RuntimeException("ZipCode required for US tax");
        }

        Map<String, Object> shipTo = new HashMap<>();
        shipTo.put("line1", "1111 S Figueroa St");
        shipTo.put("city", "Los Angeles");
        shipTo.put("region", "CA");
        shipTo.put("country", "US");
        shipTo.put("postalCode", request.getZipCode());

        Map<String, Object> shipFrom = new HashMap<>();
        shipFrom.put("line1", "3120 Palm Way");
        shipFrom.put("city", "Austin");
        shipFrom.put("region", "TX");
        shipFrom.put("country", "US");
        shipFrom.put("postalCode", "78758");

        Map<String, Object> body = new HashMap<>();
        body.put("type", "SalesOrder");
        body.put("companyCode", "ACS"); // your real code
        body.put("commit", false);
        body.put("date", LocalDate.now().toString());
        body.put("customerCode", "CUST1");
        body.put("addresses", Map.of("shipFrom", shipFrom, "shipTo", shipTo));
        body.put("lines", List.of(
                Map.of(
                        "number", "1",
                        "quantity", 1,
                        "amount", request.getPrice(),
                        "taxCode", "P0000000"
                )
        ));

        Map response = webClient.post()
                .uri("/api/v2/transactions/create")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        double taxAmount = Double.parseDouble(response.get("totalTax").toString());

        return TaxResponseDTO.builder()
                .price(request.getPrice())
                .taxAmount(taxAmount)
                .finalPrice(request.getPrice() + taxAmount)
                .taxType("AVALARA")
                .build();
    }
}