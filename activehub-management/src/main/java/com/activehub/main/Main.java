package com.activehub.main;

import java.time.LocalDate;
import java.time.LocalTime;

import com.activehub.Service.BookingSystem;
import com.activehub.repository.CatalogueRepo;


public class Main {
    public static void main(String[] args) {
        System.out.println("Active Hub Centre");
        CatalogueRepo cat = new CatalogueRepo();

        cat.displayCatalogue();

        BookingSystem bookingService = new BookingSystem();
        LocalDate testDate = LocalDate.now();

        System.out.println("--- 1. Creating Initial Booking (10:00 - 12:00) ---");
        bookingService.createBooking("Tony Stark", "012-3456789", "FC-01",
                testDate, LocalTime.of(10, 0), 2, 4);

        System.out.println("--- 2. Attempting Conflicting Booking (11:00 - 13:00) ---");
        try {
            bookingService.createBooking("Bruce Banner", "019-8765432", "FC-01",
                    testDate, LocalTime.of(11, 0), 2, 4);
        } catch (IllegalStateException e) {
            System.out.println("System Alert: " + e.getMessage());
        }

        System.out.println("\n--- 3. Booking Valid Non-Overlapping Slot (12:00 - 14:00) ---");
        bookingService.createBooking("Steve Rogers", "011-2233445", "FC-01",
                testDate, LocalTime.of(12, 0), 2, 8);

        System.out.println("\n--- 4. Active Booking Ledger ---");
        bookingService.displayBookings();
    }
}