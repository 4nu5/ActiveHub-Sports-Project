package com.activehub.Module;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Booking {
    private final String bookingId;
    private final String custName;
    private final String custContact;
    private final String facilityCode;
    private final LocalDate bookingDate;
    private final LocalTime bookingTime;
    private final int durationHours;
    private final int amountOfPeople;
    private boolean cancel;


    public Booking(String bookingID, String custName, String custContact, String facilityCode, 
        LocalDate bookingDate, LocalTime bookingTime, int durationHours, int amountOfPeople){
        if(bookingID == null || bookingID.trim().isEmpty()){
            throw new IllegalArgumentException("Booking Id cannot be blank");
        }
        if(custName == null || custName.trim().isEmpty()){
            throw new IllegalArgumentException("Customer name is Blank");
        }
        if(custContact == null || custContact.trim().isEmpty()){
            throw new IllegalArgumentException("Customer Contact is Blank");
        }
        if(facilityCode == null || facilityCode.trim().isEmpty()){
            throw new IllegalArgumentException("Facility Code is Blank");
        }
        if(durationHours < 0){
            throw new IllegalArgumentException("Duration of Time cannot be Negative");
        }
        if(amountOfPeople <= 0){
            throw new IllegalArgumentException("Participant Count Cannot be negative or 0");
        }

        this.amountOfPeople = amountOfPeople;
        this.bookingDate = Objects.requireNonNull(bookingDate, "Booking Date Was not Selected");
        this.bookingTime = Objects.requireNonNull(bookingTime, "Booking Time Was Not Selected");
        this.bookingId = bookingID.trim().toUpperCase();
        this.custContact = custContact.trim();
        this.custName = custName.trim().toUpperCase();
        this.durationHours = durationHours;
        this.facilityCode = facilityCode.trim().toUpperCase();
        this.cancel = false;
    }
    public String getBookingID(){
        return bookingId;
    }
    public String getCustName(){
        return custName;
    }
    public String getCustContact(){
        return custContact;
    }
    public LocalDate getBookingDate(){
        return bookingDate;
    }
    public LocalTime getBookingTime(){
        return bookingTime;
    }
    public int getDurationHours(){
        return durationHours;
    }
    public int getAmountofPeople(){
        return amountOfPeople;
    }
    public String getFacilityCode(){
        return facilityCode;
    }
    public boolean isCancelled(){
        return cancel;
    }
    public void Cancelled(){
        this.cancel = true;
    }
    public LocalTime getEndTime(){
        return bookingTime.plusHours(durationHours);
    }

    @Override 
    public String toString(){
        return String.format("[%s] %s | Court: %s | Date: %s | %s-%s | %d pax | %s", 
        bookingId,custName,custContact,facilityCode,bookingDate,bookingTime,
        getEndTime(),amountOfPeople,cancel ? "Cancelled" : "Active");
    }
}
