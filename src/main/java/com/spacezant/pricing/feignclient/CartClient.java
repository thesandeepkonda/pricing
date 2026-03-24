package com.spacezant.pricing.feignclient;


import com.spacezant.pricing.dto.product.ProductPriceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "cart-service", url = "${cart.service.url}")
public interface CartClient {

    @PostMapping("/api/v1/products/prices")
    List<ProductPriceDTO> getPrices(@RequestBody List<Long> varientIds);
    @GetMapping("/price/{id}")
    ProductPriceDTO getVariant(@PathVariable Long id);
}

