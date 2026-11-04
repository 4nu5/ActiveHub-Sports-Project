package com.activehub.payment;

public class payEwallet implements Payment{
    private final double amountPayed;
    private final String EwalletProvider;
    private String phoneAcc;
    private final String referenceId;

    public payEwallet(double amountPayed, String EwalletProvider, String phoneAcc, String referenceId){
        if(amountPayed < 0.0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(EwalletProvider == null || EwalletProvider.trim().isEmpty()){
            throw new IllegalArgumentException("Please List a Provider");
        }
        this.amountPayed = amountPayed;
        this.EwalletProvider = EwalletProvider.trim();
        this.phoneAcc = phoneAcc;
        this.referenceId = referenceId.trim().toUpperCase();
    }
    @Override
    public String getPaymentMethod(){
        return "E-Wallet";
    }
    @Override
    public double getAmount(){
        return amountPayed;
    }
    @Override
    public boolean processPayment(){
        return true;
    }
    @Override
    public String getPaymentDetails(){
        return String.format("Method: E-WALLET (%s) | Account: %s | Ref: %s", EwalletProvider, phoneAcc, referenceId);
    }
}
