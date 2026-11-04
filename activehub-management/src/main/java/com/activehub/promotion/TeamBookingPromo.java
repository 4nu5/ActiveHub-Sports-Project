package com.activehub.promotion;

import com.activehub.Module.Booking;
import com.activehub.Module.ItemCategory;
import com.activehub.Module.rentalTransaction;

public class TeamBookingPromo implements Promotion{
     
    private final double TeamPaxAmount = 5.0;
    private final double cappedAmount = 60.0;

    @Override
    public String getPromoCode(){
        return "Promo-B";
    }
    @Override 
    public String getPromoName(){
        return "Team Booking Reward (RM 5/ PAX, MAX RM 60)";
    }
      @Override 
    public boolean isEligible(rentalTransaction transaction){
        Booking booking = transaction.getLinkBooking();
        if(booking == null || booking.isCancelled()){
            return false;
        }
        return booking.getAmountofPeople() >= 8 && transaction.getSubtotalByCategory(ItemCategory.FACILITY) > 0.0;
    }
    @Override 
    public double calculateDiscount(rentalTransaction transaction){
        if(!isEligible(transaction)){
            return 0.0;
        }
    Booking booking = transaction.getLinkBooking();
    double teamDiscount = booking.getAmountofPeople() * TeamPaxAmount;
    double capDiscount = Math.min(teamDiscount, cappedAmount);

    double facilitySubtotal = transaction.getSubtotalByCategory(ItemCategory.FACILITY);
    return Math.min(capDiscount, facilitySubtotal);
    }
}
