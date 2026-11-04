package com.activehub.promotion;


import com.activehub.Module.rentalTransaction;

public interface Promotion{
    String getPromoCode();
    String getPromoName();
    boolean isEligible(rentalTransaction transaction);
    double calculateDiscount(rentalTransaction transaction);
}