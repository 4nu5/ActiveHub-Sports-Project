package com.activehub.promotion;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.activehub.Module.Booking;
import com.activehub.Module.ItemCategory;
import com.activehub.Module.rentalTransaction;

/**
 * Concrete Promotion Strategy implementing Promotion.
 * Evaluates eligibility and computes targeted discount savings.
 */


public class OutPeakPromo implements Promotion {
    private static final LocalTime NonPeakHourStart = LocalTime.of(9, 0);
    private static final LocalTime NonPeakHourEnd = LocalTime.of(16, 0);

    @Override
    public String getPromoCode(){
        return "Promo-A";
    }
    @Override 
    public String getPromoName(){
        return "Off Peak Saver Discount (15% off)";
    }
    @Override 
    public boolean isEligible(rentalTransaction transaction){
        Booking booking = transaction.getLinkBooking();
        if(booking == null || booking.isCancelled()){
            return false;
        }
        double facilitySubtotal = transaction.getSubtotalByCategory(ItemCategory.FACILITY);
        if(facilitySubtotal <= 0.0){
            return false;
        }
        DayOfWeek day = booking.getBookingDate().getDayOfWeek();
        boolean isWeekday = (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY);

        LocalTime start = booking.getBookingTime();
        boolean isOffPeak = (!start.isBefore(NonPeakHourStart) && start.isBefore(NonPeakHourEnd));
        return isWeekday && isOffPeak;
    }
    @Override 
    public double calculateDiscount(rentalTransaction transaction){
        if (!isEligible(transaction)){
            return 0.0;
        }
        double facilitySubtotal = transaction.getSubtotalByCategory(ItemCategory.FACILITY);
        return facilitySubtotal * 0.15;
    }
}
