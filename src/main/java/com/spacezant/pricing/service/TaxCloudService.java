package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.tax.TaxRequestDTO;
import com.spacezant.pricing.dto.tax.TaxResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaxCloudService {

    private final WebClient webClient;

    @Value("${taxcloud.api.login.id}")
    private String apiLoginId;

    @Value("${taxcloud.api.key}")
    private String apiKey;

    public TaxResponseDTO calculateTax(TaxRequestDTO request) {

        try {
            Map<String, Object> body = Map.of(
                    "apiLoginID", apiLoginId,
                    "apiKey", apiKey,
                    "customerID", "1",
                    "cartID", "1",
                    "cartItems", List.of(
                            Map.of(
                                    "Index", 0,
                                    "ItemID", "SKU1",
                                    "Price", request.getPrice(),
                                    "Qty", 1,
                                    "TIC", "00000"
                            )
                    ),
                    "origin", Map.of(
                            "Address1", "3120 Palm Way",
                            "City", "Austin",
                            "State", "TX",
                            "Zip5", "78758"
                    ),
                    "destination", Map.of(
                            "Address1", "1111 S Figueroa St",
                            "City", "Los Angeles",
                            "State", "CA",
                            "Zip5", request.getZipCode()
                    ),
                    "deliveredBySeller", false
            );

            Map response = webClient.post()
                    .uri("/1.0/taxcloud/Lookup")   // ✅ CORRECT ENDPOINT
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("✅ TaxCloud response: " + response);

            double taxAmount = extractTax(response);

            return TaxResponseDTO.builder()
                    .price(request.getPrice())
                    .taxAmount(taxAmount)
                    .totalTaxPercentage((taxAmount / request.getPrice()) * 100)
                    .finalPrice(request.getPrice() + taxAmount)
                    .taxType("SALES_TAX")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("TaxCloud API failed: " + e.getMessage());
        }
    }

    private double extractTax(Map response) {
        List<Map> items = (List<Map>) response.get("CartItemsResponse");

        if (items == null || items.isEmpty()) {
            System.out.println("⚠️ TaxCloud returned empty response: " + response);

            return 0; // fallback safely
        }

        double tax = 0;
        for (Map item : items) {
            tax += Double.parseDouble(item.get("TaxAmount").toString());
        }

        return tax;
    }
}