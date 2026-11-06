package com.activehub.promotion;

/**
 * Strategy Pattern Interface: Defines the contract for all discounts.
 * Supports Polymorphism by allowing varied discount calculations at runtime.
 */

import com.activehub.Module.rentalTransaction;

public interface Promotion{
    String getPromoCode();
    String getPromoName();
    boolean isEligible(rentalTransaction transaction);
    double calculateDiscount(rentalTransaction transaction);
}