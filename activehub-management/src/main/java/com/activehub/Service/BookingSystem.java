package com.activehub.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.activehub.Module.Booking;


public class BookingSystem {
    private final Map<String, Booking> bookingTable = new LinkedHashMap<>();
    private int Counter = 1000;

    private synchronized String generateID(){
        Counter++;
        return "BO - " + Counter;
    }
    public boolean collisionCheck(String facilityCode, LocalDate bookingDate, LocalTime bookingTime, int durationHours){
        LocalTime proposedEndTime = bookingTime.plusHours(durationHours);

        for(Booking exists : bookingTable.values()){
            if(exists.isCancelled()) continue;

            boolean sameFacility = exists.getFacilityCode().equalsIgnoreCase(facilityCode.trim());
            boolean sameDate = exists.getBookingDate().equals(bookingDate);

            if(sameDate && sameFacility){
                LocalTime existingStart = exists.getBookingTime();
                LocalTime existingEnd = exists.getEndTime();

                boolean hasOverlap = bookingTime.isBefore(existingEnd) && existingStart.isBefore(proposedEndTime);
                if(hasOverlap){
                    return true;
                }
            }
        }
        return false;
    }

    
}
