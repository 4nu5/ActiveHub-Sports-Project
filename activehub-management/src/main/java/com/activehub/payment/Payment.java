package com.activehub.payment;

/**
 * Interface for Payment Processing demonstrating Polymorphism.
 * Specific gateways (payCash, payCard, payEwallet) implement concrete authorization.
 */

public interface Payment {
    String getPaymentMethod();
    double getAmount();
    String getPaymentDetails();
    boolean processPayment();
}
