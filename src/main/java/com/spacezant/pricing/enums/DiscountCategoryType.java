package com.spacezant.pricing.enums;

public enum DiscountCategoryType {

    // Core pricing logic
    PERCENTAGE,          // 20% OFF
    FIXED,               // ₹100 OFF

    // Quantity-based
    BULK,                // Buy more → discount
    BUY_X_GET_Y,         // BOGO

    // Business / strategy
    TRADE,               // Vendor-level discount
    PROMOTIONAL,         // Festival / seasonal

    // Customer retention
    LOYALTY,             // Repeat customers

    // Advanced pricing
    BUNDLE,              // Combo offers

    // Logistics
    FREE_SHIPPING,
    FREE_GIFT, FLAT;

}

