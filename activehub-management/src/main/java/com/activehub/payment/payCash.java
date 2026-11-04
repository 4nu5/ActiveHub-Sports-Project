package com.activehub.payment;

public class payCash implements Payment{
    private final double amountPayed;
    private final double cashReceived;
    private  double change;

    public payCash(double amountPayed, double cashReceived){
        if(amountPayed < 0.0){
            throw new IllegalArgumentException("Amount Payed Was Negative");
        }
        if(cashReceived < 0.0){
            throw new IllegalArgumentException("Amount received cannot be negative");
        }
        this.amountPayed = amountPayed;
        this.cashReceived = cashReceived;
    }
    @Override 
    public String getPaymentMethod(){
        return "Cash";
    }
     @Override 
    public double getAmount(){
        return amountPayed;
    }
     @Override 
    public double getChange(){
        return change;
    }
    @Override
    public boolean processPayment(){
        if(cashReceived >= amountPayed){
            this.change = cashReceived - amountPayed;
            return true;
    }
        return false;
    }
    @Override
    public String getPaymentDetails(){
        return String.format("Method: Cash | Received: RM %.2f | Change: RM %.2f", cashReceived, change);
    }
}
