package com.spacezant.pricing.controller;

import com.spacezant.pricing.dto.CartCouponRequest;
import com.spacezant.pricing.dto.CartCouponResponseDTO;
import com.spacezant.pricing.dto.CartItemPricingDTO;
import com.spacezant.pricing.dto.coupon.CreateCouponRequestDTO;
import com.spacezant.pricing.dto.coupon.CouponResponseDTO;
import com.spacezant.pricing.entity.Coupon;
import com.spacezant.pricing.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/calculate")
    public ResponseEntity<CartCouponResponseDTO> calculateCartPrice(@RequestBody CartCouponRequest request) {
        // This calls the service that handles Product Price fetching and Coupon logic
        CartCouponResponseDTO response = couponService.calculatePrice(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCoupon(@RequestBody CreateCouponRequestDTO request) {

        couponService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Coupon created successfully");
    }

    @GetMapping("/allCoupons")
    public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {

        return ResponseEntity.ok(couponService.getAllCoupons());
    }
    // ✅ 4. Apply Coupon (MAIN API 🔥)
    @PostMapping("/apply")
    public ResponseEntity<CartCouponResponseDTO> applyCoupon(
            @RequestParam String couponCode,
            @RequestParam String userId,
            @RequestBody List<CartItemPricingDTO> items) {

        CartCouponResponseDTO response =
                couponService.applyCoupon(couponCode, userId, items);

        return ResponseEntity.ok(response);
    }
    // ✅ 5. Record Coupon Usage (Call after order success)
    @PostMapping("/usage")
    public ResponseEntity<String> recordUsage(
            @RequestParam String couponCode,
            @RequestParam String userId,
            @RequestParam String orderId,
            @RequestParam Double discountAmount) {

        Coupon coupon = couponService.getByCode(couponCode);

        couponService.recordUsage(coupon, userId, orderId, discountAmount);

        return ResponseEntity.ok("Coupon usage recorded");
    }
}