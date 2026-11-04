package com.activehub.payment;

public interface Payment {
    String getPaymentMethod();
    double getAmount();
    String getPaymentDetails();
    boolean processPayment();
}
