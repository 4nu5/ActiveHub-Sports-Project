package com.activehub.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.activehub.Module.Booking;
import com.activehub.Module.RentalLine;
import com.activehub.Module.rentalTransaction;
import com.activehub.payment.Payment;

public class Reporting {

    private ArrayList<Map.Entry<String, Integer>> List;
    public static class TransactionRecord{
        private final String transactionId;
        private final String customerName;
        private final double rawSubtotal;
        private final double discount;
        private final double SST;
        private final double finalTotal;
        private final String paymentMethod;
        private final List<RentalLine> line;


        public TransactionRecord(String transactionId, String customerName, double rawSubtotal, double discount, double SST, double finalTotal, String paymentMethod, List<RentalLine> line){
            this.transactionId = transactionId;
            this.customerName = customerName.toUpperCase();
            this.rawSubtotal = rawSubtotal;
            this.discount = discount;
            this.SST = SST;
            this.finalTotal = finalTotal;
            this.paymentMethod = paymentMethod;
            this.line = new ArrayList<>(line);
        }
        public String getTransactionId() {
            return transactionId;
        }
        public String getCustomerName() {
            return customerName;
        }
        public double getRawSubtotal() {
            return rawSubtotal;
        }
        public double getDiscount() {
            return discount;
        }
        public double getSST() {
            return SST;
        }
        public double getFinalTotal() {
            return finalTotal;
        }
        public String getPaymentMethod() {
            return paymentMethod;
        }
        public List<RentalLine> getLine() {
            return line;
        }
    }
        private final List<TransactionRecord> dailyRecords = new ArrayList<>();

        public void recordCompleteTransaction(rentalTransaction trans, double discount, double SST, double finalTotal, Payment payment){
            
            List<RentalLine> line = new ArrayList<>(trans.getRentalLines());
            dailyRecords.add(new TransactionRecord(
            trans.getTransactionId(),
            trans.getCustomerName(),
            trans.getPreDisSubtotal(),
            discount, SST, finalTotal, payment.getPaymentMethod(),line));
        }

        public void generateDailyReport(Collection<Booking> allBooking){
            String Border = "=================================================";
            String Border2 = "-------------------------------------------------";
            System.out.println(Border);
            System.out.println("  Active Hub Sports Centre - Report (Daily)  ");
            System.out.println("            Date: " + LocalDateTime.now());
            System.out.println(Border);

            long totalBookings = allBooking.size();
            long activeBookings = allBooking.stream().filter(b -> !b.isCancelled()).count();
            long cancelBooking = totalBookings - activeBookings;

            System.out.println("1. Facility Booking Metrics");
            System.out.println(Border2);
            System.out.printf("  Total Bookings Logged : %d\n", totalBookings);
            System.out.printf("  Active Reservations   : %d\n", activeBookings);
            System.out.printf("  Cancellations         : %d\n", cancelBooking);
            System.out.println(Border2);

            double grossAmount = 0.0;
            double totalDiscounts = 0.0;
            double totalServiceCharge = 0.0;
            double revenue = 0.0;
            Map<String, Integer> paymentCount = new LinkedHashMap<>();
            Map<String, Integer> itemFrequent = new HashMap<>();

            for(TransactionRecord record : dailyRecords){
                grossAmount += record.getRawSubtotal();
                totalDiscounts += record.getDiscount();
                totalServiceCharge += record.getSST();
                revenue += record.getFinalTotal();

                paymentCount.put(record.getPaymentMethod(), paymentCount.getOrDefault(record.getPaymentMethod(), 0) + 1);

                for(RentalLine line : record.getLine()){
                    String itemKey = line.getItem().getItemCode() + " - " + line.getItem().getItemName();
                    itemFrequent.put(itemKey, itemFrequent.getOrDefault(itemKey, 0) + line.getQuantity());
                }
            }
            System.out.println("2. Financial Revenue Summary");
            System.out.println(Border2);
            System.out.printf("  Completed Transactions : %d\n", dailyRecords.size());
            System.out.printf("  Gross Sales (Raw)      : RM %10.2f\n", grossAmount);
            System.out.printf("  Total Discounts Given  : RM %10.2f\n", totalDiscounts);
            System.out.printf("  Service Charges (10%%)  : RM %10.2f\n", totalServiceCharge);
            System.out.printf("  NET TOTAL REVENUE      : RM %10.2f\n", revenue);

            if(!dailyRecords.isEmpty()){
                System.out.printf(" Average Order Value :  RM %10.2f\n", (revenue / dailyRecords.size()));
            }
            System.out.println(Border2);
            
            System.out.println("3. Payment Method Breakdown");
            if(paymentCount.isEmpty()){
                System.out.println(" No Transaction done Today");
            }else{
                for(Map.Entry<String, Integer> entry : paymentCount.entrySet()){
                    System.out.printf("%-25s : %d transaction(s)\n", entry.getKey(), entry.getValue());
                }
            }
            System.out.println(Border2);
        
        

            System.out.println("4. Inventory / Service Usage Frequency");
            if(itemFrequent.isEmpty()){
                System.out.println("No Items / Services were rented");
            }
            else {
                itemFrequent.entrySet().stream()
                    .sorted((entryA, entryB) -> entryB.getValue().compareTo(entryA.getValue()))
                    .forEach(entry -> 
                        System.out.printf("  %-35s : %3d unit(s) utilized\n", entry.getKey(), entry.getValue())
                    );
            }
            System.out.println(Border + "\n");
        }
    }

