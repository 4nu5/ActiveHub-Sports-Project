package com.activehub.persistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import com.activehub.Module.Booking;
import com.activehub.Service.BookingSystem;

/**
 * Helps make the way to save details of past 
 * records after the program has been shut down
 */

public class ServicePersistance {
    private static final String DATA_Dir = "Data";
    private static final String Bookings_File = DATA_Dir + File.separator + "bookings.txt";
    private static final String Transaction_File = DATA_Dir + File.separator + "transaction.txt";

    public ServicePersistance(){
        File dir = new File(DATA_Dir);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    private static File resolveDataFile(String fileName) {
        File nestedDir = new File("activehub-management/Data");
        File localDir = new File("Data");

        File targetDir = nestedDir.exists() || new File("activehub-management").exists()
                ? nestedDir
                : localDir;

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        return new File(targetDir, fileName);
    }

/**
 * File I/O: Writes active bookings to disk. Using the format below
 * Schema: BookingID|CustomerName|ContactNumber|FacilityCode|Date|Time|Duration|Pax|Cancelled
 */
   public void saveBookings(Collection<Booking> bookings) {
    File file = resolveDataFile("bookings.txt");
    try (PrintWriter write = new PrintWriter(new FileWriter(file, false))) {
        for (Booking b : bookings) {
            write.printf("%s|%s|%s|%s|%s|%s|%d|%d|%b%n",
                b.getBookingID(),
                b.getCustName(),
                b.getCustContact(),
                b.getFacilityCode(),
                b.getBookingDate().toString(),
                b.getBookingTime().toString(),
                b.getDurationHours(),
                b.getAmountofPeople(),
                b.isCancelled()
            );
        }
    } catch (IOException e) {
        System.err.println("Error: Failed to Write to " + file.getAbsolutePath() + ": " + e.getMessage());
    }
}
    /**
 * Reloads booking from disk into memory upon system startup,
 * parsing records.
 */
    public void loadBookings(BookingSystem bookingService){
        File file = resolveDataFile("bookings.txt");
        if(!file.exists()){
             return;
        }

        try(BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;
            while((line = read.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                    String[] token = line.split("\\|");
                    if(token.length >= 9){
                        String customer = token[1];
                        String contact = token[2];
                        String facility = token[3];
                        LocalDate date = LocalDate.parse(token[4]);
                        LocalTime time = LocalTime.parse(token[5]);
                        int duration = Integer.parseInt(token[6]);
                        int pax = Integer.parseInt(token[7]);
                        boolean cancel = Boolean.parseBoolean(token[8]);

                        Booking booking = bookingService.createBooking(customer, contact, facility, date, time, duration, pax);
                        if(cancel){
                            booking.Cancelled();
                        }
               }
            }
        }catch(Exception e){
            System.err.println("Error: Could not Read " + Bookings_File + e.getMessage());
        }
    }
    /**
 * Appends settled transaction receipts to the permanent audit log.
 */
    public void appendTransRecord(String transId, String customer, double rawSubtotal, double discount, double serviceCharge, double finalTotal, String paymentSummary){
        try(PrintWriter write = new PrintWriter(new FileWriter(Transaction_File, true))){
            write.printf("%s|%s|%.2f|%.2f|%.2f|%.2f|%s%n", transId, customer, rawSubtotal, discount, serviceCharge, paymentSummary);
        }catch(IOException e){
            System.err.println("Error: Failed to append to " + Transaction_File + e.getMessage());
        }
    }
}
