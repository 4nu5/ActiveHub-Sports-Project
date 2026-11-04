package com.activehub.Module;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public Collection<RentalLine> gRentalLines(){
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
    
}
