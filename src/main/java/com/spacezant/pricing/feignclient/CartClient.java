package com.spacezant.pricing.feignclient;


import com.spacezant.pricing.dto.product.PricingInfo;
import com.spacezant.pricing.dto.product.ProductPriceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(name = "cart-service", url = "${cart.service.url}")
public interface CartClient {

    @PostMapping("/api/v1/products/prices")
    List<ProductPriceDTO> getPrices(@RequestBody List<Long> varientIds);
    @GetMapping("/price/{id}")
    ProductPriceDTO getVariant(@PathVariable Long id);

    @FeignClient(name = "pricing-service")
    public interface PricingFeignClient {

        @PostMapping("/api/variant-pricing/bulk")
        Map<Long, PricingInfo> getBulkPricing(
                @RequestBody List<Long> variantIds,
                @RequestParam("countryCode") String countryCode
        );
    }

}

