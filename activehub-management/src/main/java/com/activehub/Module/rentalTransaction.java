package com.activehub.Module;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the shopping lifecycle, association with court bookings,
 * and tracks itemised rental charges prior to settlement.
 */

public class rentalTransaction {
    private final String transactionId;
    private final String customerName;
    private final String customerContact;
    private final Booking linkBooking;
    private final LocalDateTime TimeStamp;
    private final Map<String, RentalLine> lineItems = new HashMap<>();


    private rentalTransaction(String transactionId, String customerName, String customerContact, Booking linkBooking, LocalDateTime TimeStamp){
        if(transactionId == null || transactionId.isEmpty()){
            throw new IllegalArgumentException("Transaction ID is empty");
        }
        if(customerName == null || customerName.trim().isEmpty()){
            throw new IllegalArgumentException("Customer name is Empty");
        }
        if(customerContact == null || customerContact.trim().isEmpty()){
            throw new IllegalArgumentException("Customer Contact is Empty");
        }
        this.transactionId = transactionId.trim().toUpperCase();
        this.customerName = customerName.trim().toUpperCase();
        this.customerContact = customerContact.trim();
        this.linkBooking = linkBooking;
        this.TimeStamp = Objects.requireNonNull(TimeStamp, "TimeStamp is empty");
    }
    public rentalTransaction(String transactionId, Booking linkBooking){
         this(transactionId, linkBooking.getCustName(), linkBooking.getCustContact(), linkBooking, LocalDateTime.now());
    }
    public rentalTransaction(String transactionId, String customerName, String customerContact){
        this(transactionId, customerName, customerContact, null, LocalDateTime.now());
    }
        public String getTransactionId() {
        return transactionId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerContact() {
        return customerContact;
    }
    public Booking getLinkBooking() {
        return linkBooking;
    }
    public LocalDateTime getTimeStamp() {
        return TimeStamp;
    }
    public Collection<RentalLine> getRentalLines(){
        return Collections.unmodifiableCollection(lineItems.values());
    }
    public void addItem(Item item, int quantity){
        Objects.requireNonNull(item, "Item is empty cant continue with transaction");
        String code = item.getItemCode();
        if(lineItems.containsKey(code)){
            lineItems.get(code).AddItem(quantity);
        }else{
            lineItems.put(code, new RentalLine(item, quantity));
        }
    }
    public double getSubtotalByCategory(ItemCategory category){
        double subtotal = 0.0;
        for(RentalLine line : lineItems.values()){
            subtotal += line.getTotalRentalItemPrice();
        }
        return subtotal;
    }
    public int getTotalEquipment(){
        int count = 0; 
        for(RentalLine line : lineItems.values()){
            if(line.getItem().getCategory() == ItemCategory.EQUIPMENT){
                count += line.getQuantity();
            }
        }
        return count;
    }
    public double getPreDisSubtotal(){
        double total = 0;
        for (RentalLine line : lineItems.values()) {
            total += line.getTotalRentalItemPrice();
        }
        return total;
    }
     public void displayReceipt() {
        String border = "=================================================================";
        System.out.println(border);
        System.out.println("                   ACTIVEHUB TRANSACTION RECEIPT                 ");
        System.out.println(border);
        System.out.printf("Txn ID: %-16s Date: %s\n", transactionId, TimeStamp.toLocalDate());
        System.out.printf("Customer: %-14s Contact: %s\n", customerName, customerContact);
        if (linkBooking != null) {
            System.out.printf("Linked Booking: %-10s Court: %s (%s, %d hrs)\n",
                    linkBooking.getBookingID(), linkBooking.getFacilityCode(),
                    linkBooking.getBookingTime(), linkBooking.getDurationHours());
        } else {
            System.out.println("Type: WALK-IN (No Court Booking)");
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-6s | %-24s | %-16s | %-9s\n", "Code", "Item Description", "Qty x Price", "Total");
        System.out.println("-----------------------------------------------------------------");

        for (RentalLine line : lineItems.values()) {
            System.out.println(line);
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("Facility Subtotal:                                    RM %7.2f\n", getSubtotalByCategory(ItemCategory.FACILITY));
        System.out.printf("Equipment Subtotal:                                   RM %7.2f\n", getSubtotalByCategory(ItemCategory.EQUIPMENT));
        System.out.printf("Service Subtotal:                                     RM %7.2f\n", getSubtotalByCategory(ItemCategory.SERVICE));
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("RAW SUBTOTAL:                                         RM %7.2f\n", getPreDisSubtotal());
        System.out.println(border);
    }
}
