package com.activehub.payment;

public class payCard implements Payment{
    private final String cardNum;
    private final double amountPayed;
    private final String cardType;

    public payCard(String cardNum, double amountPayed, String cardType){
         if(amountPayed < 0.0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(cardNum == null || cardNum.trim().length() < 12){
            throw new IllegalArgumentException("Please Provide a Card Number");
        }
        this.amountPayed = amountPayed;
        this.cardNum = maskCardNum(cardNum.trim());
        this.cardType = cardType.trim().toUpperCase();
    }
    private String maskCardNum(String Cardnum){
        String clean = Cardnum.replaceAll("\\s+", "");
        String lastFourDigit = clean.substring(clean.length() - 4);
        return "****-****-****-" + lastFourDigit;
    }
    @Override 
    public String getPaymentMethod(){
        return "Card (" + cardType + ")";
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
        return String.format("Method: CARD (%s) | Card No: %s | Auth: APPROVED", cardType, cardNum);
    }

}
