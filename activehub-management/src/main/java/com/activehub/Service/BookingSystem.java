package com.activehub.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
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

public Booking createBooking(String custName, String custContact, String facilityCode, LocalDate bookingDate, LocalTime bookingTime, int durationHours, int amountOfPeople){
    if(collisionCheck(facilityCode, bookingDate, bookingTime, durationHours)){
        throw new IllegalArgumentException(String.format("Collision detected: %s is already reserved during %s to %s on %s.", facilityCode, bookingDate, bookingTime, durationHours));
    }
    String id = generateID();
    Booking newBooking = new Booking(id, custName, custContact, facilityCode, bookingDate, bookingTime, durationHours, amountOfPeople);
    bookingTable.put(id, newBooking);
    return newBooking;
}

    public Booking findBooking(String bookingId){
        if(bookingId == null) return null;
        return bookingTable.get(bookingId.trim().toUpperCase());
}
    public Collection<Booking> getAllBookings(){
        return Collections.unmodifiableCollection(bookingTable.values());   
    }
    public void displayBookings(){
        System.out.println("--------------------------------------------------------");
        System.out.printf("| %-8s | %-20s | %-10s | %-10s | %-13s | %-7s | %-9s |\n", "ID", "Customer", 
        "Facility", "Date" ,"Slot", "Pax", "Status");
        System.out.println("--------------------------------------------------------");

        for(Booking b : bookingTable.values()){
            String slot = b.getBookingTime() + "-" + b.getEndTime();
            System.out.printf("| %-8s | %-20s | %-10s | %-10s | %-13s | %-7d | %-9s |\n", b.getBookingID(), b.getCustName(), 
            b.getFacilityCode(), b.getBookingDate(), slot, b.getAmountofPeople(), b.isCancelled() ? "Cancelled" : "Active");

        }
         System.out.println("--------------------------------------------------------");
    }
}


