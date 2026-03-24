package com.spacezant.pricing.feignclient;

import com.spacezant.pricing.dto.product.ProductPriceDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MockProductController {
    @PostMapping("/api/v1/products/prices")
    public List<ProductPriceDTO> getMockPrices(@RequestBody List<Long> ids) {
        // Return hardcoded prices for testing
        return ids.stream().map(id -> {
            ProductPriceDTO p = new ProductPriceDTO();
            p.setVarientId(id);
            p.setPrice(1000.0); // Everything costs 100
            return p;
        }).toList();
    }
}
