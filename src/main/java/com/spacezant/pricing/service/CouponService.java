package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.*;
import com.spacezant.pricing.dto.coupon.CouponResponseDTO;
import com.spacezant.pricing.dto.coupon.CreateCouponRequestDTO;
import com.spacezant.pricing.dto.product.ProductPriceDTO;
import com.spacezant.pricing.entity.Coupon;
import com.spacezant.pricing.entity.CouponUsage;
import com.spacezant.pricing.feignclient.CartClient;
import com.spacezant.pricing.repository.CouponRepository;
import com.spacezant.pricing.repository.CouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CartClient productClient;
    private final DiscountService discountService;
    private final CouponRepository couponRepository;
    private final CouponUsageRepository usageRepository;

    public void create(CreateCouponRequestDTO request) {

        couponRepository.findByCouponCode(request.getCouponCode())
                .ifPresent(c -> {
                    throw new RuntimeException("Coupon already exists");
                });

        Coupon coupon = new Coupon();
        coupon.setCouponCode(request.getCouponCode());
        coupon.setCouponName(request.getCouponName());
        coupon.setDescription(request.getDescription());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setCurrencyCode(request.getCurrencyCode());
        coupon.setStartDate(request.getStartDate());
        coupon.setEndDate(request.getEndDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setPerUserLimit(request.getPerUserLimit());
        coupon.setStatus(request.getStatus());
        coupon.setTotalUsed(0);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());

        couponRepository.save(coupon);
    }

    public List<CouponResponseDTO> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Coupon getByCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    private CouponResponseDTO mapToDTO(Coupon coupon) {
        CouponResponseDTO dto = new CouponResponseDTO();

        dto.setCouponId(coupon.getCouponId());
        dto.setCouponCode(coupon.getCouponCode());
        dto.setCouponName(coupon.getCouponName());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMaxDiscountAmount(coupon.getMaxDiscountAmount());
        dto.setMinOrderAmount(coupon.getMinOrderAmount());
        dto.setCurrencyCode(coupon.getCurrencyCode());
        dto.setStartDate(coupon.getStartDate());
        dto.setEndDate(coupon.getEndDate());
        dto.setStatus(coupon.getStatus());

        return dto;
    }

    public void validate(Coupon coupon, String userId, Long cartAmount) {

        validateStatus(coupon);
        validateDate(coupon);
        validateMinOrder(coupon, cartAmount);
        validateUsageLimit(coupon);
        validateUserLimit(coupon, userId);
    }

    private void validateStatus(Coupon coupon) {
        if (!"ACTIVE".equalsIgnoreCase(coupon.getStatus())) {
            throw new RuntimeException("Coupon is inactive");
        }
    }

    private void validateDate(Coupon coupon) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(coupon.getStartDate()) ||
                today.isAfter(coupon.getEndDate())) {
            throw new RuntimeException("Coupon expired or not started");
        }
    }

    private void validateMinOrder(Coupon coupon, Long cartAmount) {
        if (cartAmount < coupon.getMinOrderAmount()) {
            throw new RuntimeException("Minimum order amount not met");
        }
    }

    private void validateUsageLimit(Coupon coupon) {

        int totalUsed = coupon.getTotalUsed() == null ? 0 : coupon.getTotalUsed();

        if (coupon.getUsageLimit() != null &&
                totalUsed >= coupon.getUsageLimit()) {
            throw new RuntimeException("Coupon usage limit exceeded");
        }
    }

    private void validateUserLimit(Coupon coupon, String userId) {

        int userUsage = usageRepository
                .countByCoupon_CouponIdAndUserId(coupon.getCouponId(), userId);

        if (coupon.getPerUserLimit() != null &&
                userUsage >= coupon.getPerUserLimit()) {
            throw new RuntimeException("User usage limit exceeded");
        }
    }
    public double calculateDiscount(Coupon coupon, Long cartTotal) {

        double discount;

        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = (cartTotal * coupon.getDiscountValue()) / 100.0;
        } else {
            discount = coupon.getDiscountValue();
        }

        return applyMaxDiscount(coupon, discount);
    }

    private double applyMaxDiscount(Coupon coupon, double discount) {

        if (coupon.getMaxDiscountAmount() != null &&
                discount > coupon.getMaxDiscountAmount()) {
            return coupon.getMaxDiscountAmount();
        }

        return discount;
    }

    public void recordUsage(Coupon coupon,
                            String userId,
                            String orderId,
                            Double discountAmount) {

        CouponUsage usage = new CouponUsage();
        usage.setUsageId(UUID.randomUUID().toString());
        usage.setUserId(userId);
        usage.setOrderId(orderId);
        usage.setDiscountAmount(discountAmount);
        usage.setUsedAt(LocalDateTime.now());
        usage.setCoupon(coupon);

        usageRepository.save(usage);

        // update total usage
        coupon.setTotalUsed(coupon.getTotalUsed() + 1);
        couponRepository.save(coupon);
    }




    // ================= COUPON =================

    public Double applyCoupon(String code, Double currentTotal) {

        if (code == null || code.isBlank()) return (double) 0;

        Coupon c = couponRepository.findByCouponCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        if (!"ACTIVE".equalsIgnoreCase(c.getStatus())) return (double) 0;

        if (c.getEndDate() != null && c.getEndDate().isBefore(LocalDate.now()))
            return (double) 0;

        if (c.getMinOrderAmount() != null &&
                currentTotal < c.getMinOrderAmount())
            return (double) 0;

        Integer totalUsed = c.getTotalUsed() != null ? c.getTotalUsed() : 0;

        if (c.getUsageLimit() != null &&
                totalUsed >= c.getUsageLimit())
            return (double) 0;

        Double discount;

        if ("PERCENTAGE".equalsIgnoreCase(c.getDiscountType())) {

            discount = (currentTotal * c.getDiscountValue()) / 100;

            if (c.getMaxDiscountAmount() != null &&
                    discount > c.getMaxDiscountAmount()) {
                discount = c.getMaxDiscountAmount();
            }

        } else {
            discount = c.getDiscountValue();
        }

        return discount;
    }


}