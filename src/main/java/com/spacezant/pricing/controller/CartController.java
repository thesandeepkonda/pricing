package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.cart.CartRequest;
import com.spacezant.pricing.dto.cart.CartResponse;
import com.spacezant.pricing.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/calculate")
    public ResponseEntity<CartResponse> calculateCart(@RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.calculateCart(request));
    }
}