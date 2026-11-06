package com.activehub.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.activehub.Module.Booking;
import com.activehub.Module.Item;
import com.activehub.Module.rentalTransaction;
import com.activehub.Service.BookingSystem;
import com.activehub.Service.Reporting;
import com.activehub.payment.Payment;
import com.activehub.payment.payCard;
import com.activehub.payment.payCash;
import com.activehub.payment.payEwallet;
import com.activehub.persistance.ServicePersistance;
import com.activehub.promotion.PromoCalculator;
import com.activehub.promotion.Promotion;
import com.activehub.repository.CatalogueRepo;




public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final CatalogueRepo cat = new CatalogueRepo();
    private static final BookingSystem book = new BookingSystem();
    private static final PromoCalculator promo  = new PromoCalculator();
    private static final Reporting report = new Reporting();
    private static final ServicePersistance persistant = new ServicePersistance();

    private static rentalTransaction activeTransaction = null;
    private static int transactionSeq = 5000;

    public static void main(String[] args) {
        String Border = "=================================================";
        System.out.println(Border);
        System.out.println("Active Hub Sports Centre");
        System.out.println(Border);

        persistant.loadBookings(book);
        System.out.println("Booking Records reloaded");

        boolean running = true;
        while(running){
            printMainMenu();
            System.out.print("Select Option 1-7: ");
            String input = sc.nextLine().trim();

            switch(input) {
                case "1":
                    BookingModule();
                    break;
                case "2":
                    cat.displayCatalogue();
                    break;
                case "3":
                    CreateTransaction();
                    break;
                case "4":
                    ApplyPromotion();
                    break;
                case "5":
                    PaymentSettlement();
                    break;
                case "6":
                    report.generateDailyReport(book.getAllBookings());
                    break;
                case "7":
                    System.out.println("Saving Files");
                    persistant.saveBookings(book.getAllBookings());
                    System.out.println("Done");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Selection. Please Input again (1-7)");
            }
        }
    }
    private static void PaymentSettlement() {
        System.out.println("Payment & Close Transaction");
        if(activeTransaction == null){
            System.out.println("No active Transactions right now");
            return;
        }

        double rawSubtotal = activeTransaction.getPreDisSubtotal();
        PromoCalculator.resultPromo result = PromoCalculator.getBestPromo(activeTransaction);
        double discount = (result.getPromo() != null) ? result.getDiscountAmount() : 0.0;
        double adjustedSubtotal = Math.max(0.0, rawSubtotal - discount);
        double serviceCharge = adjustedSubtotal * 0.10;
        double finalAmount = adjustedSubtotal + serviceCharge;

        System.out.println("\nBILLING BREAKDOWN:");
        System.out.printf("  Subtotal       : RM %7.2f\n", rawSubtotal);
        if (discount > 0.0) {
            System.out.printf("  Promo Applied      : %s (-RM %.2f)\n", result.getPromo().getPromoCode(), discount);
        }
        System.out.printf("  Adjusted Subtotal  : RM %7.2f\n", adjustedSubtotal);
        System.out.printf("  Service Charge (10%%): RM %7.2f\n", serviceCharge);
        System.out.printf("  TOTAL DUE          : RM %7.2f\n", finalAmount);

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit / Debit Card");
        System.out.println("3. E-Wallet (GrabPay / TnG / Boost)");
        System.out.print("Choice (1-3): ");

        String choice = sc.nextLine().trim();

        Payment pay = null;
        try{
        if("1".equals(choice)){
            System.out.print("Enter Cash Amount: RM ");
            double cAmount = Double.parseDouble(sc.nextLine().trim());
            payCash cash = new payCash(finalAmount, cAmount);

            if(cash.processPayment()){
                pay = cash;
                System.out.printf("Payment Accepted. \nChange: RM %.2f\n", cash.getChange());
            }else{
                System.out.println("Payment Rejected: Insufficent Funds provided");
                return;
            }
        }else if("2".equals(choice)){
            System.out.println("Enter Card Number");
            String CardNo = sc.nextLine().trim();
            System.out.print("Enter Card Type (Credit/Debit)");
            String cardType = sc.nextLine().trim();
            pay = new payCard(CardNo, finalAmount ,cardType);
            pay.processPayment();
            System.out.println("Card Authorised");
        }else if("3".equals(choice)){
            System.out.println("Enter E-Wallet Provider");
            String provider = sc.nextLine().trim();
            System.out.println("Enter Account");
            String account = sc.nextLine().trim();
            pay = new payEwallet(finalAmount, provider, account, "Ref- " + System.currentTimeMillis() % 1000000);
            pay.processPayment();
            System.out.println("E-Wallet Verified");
        }else{
            System.out.println("Invalid Payment Choice");
            return;
        }
        }catch(NumberFormatException e){
            System.out.println("Error: Amount payed was not a digit");
            return;
        }catch(IllegalArgumentException e){
            System.out.println("Payment Error " + e.getMessage());
            return;
        }
        report.recordCompleteTransaction(activeTransaction, discount, serviceCharge, finalAmount, pay);
        persistant.appendTransRecord(activeTransaction.getTransactionId(), activeTransaction.getCustomerName(), rawSubtotal, discount, serviceCharge, finalAmount, pay.getPaymentDetails());

        System.out.println("Recipt Settled: " + activeTransaction.getTransactionId() + "Complete");
        activeTransaction = null;
    }
    private static void ApplyPromotion() {
        System.out.println("Evaluate Promotions");
        if(activeTransaction == null){
            System.out.println("No Active Transactions");
            return;
        }
        PromoCalculator.resultPromo result = PromoCalculator.getBestPromo(activeTransaction);
        if(result.getPromo() == null || result.getDiscountAmount() <= 0.0){
            System.out.println("Result: No promotions are applicable");
        }else{
            Promotion p = result.getPromo();
            System.out.println("BEST PROMOTION IDENTIFIED:");
            System.out.printf("  Code       : %s\n", p.getPromoCode());
            System.out.printf("  Name       : %s\n", p.getPromoName());
            System.out.printf("  Savings    : RM %.2f\n", result.getDiscountAmount());
        }
    }
    
    private static void CreateTransaction() {
        System.out.println("Create / Modify Rental Transaction");
        if(activeTransaction == null){
            System.out.println("1. Link to Existing Court Booking");
            System.out.println("2. Walk-In Equipment / Service Rental (No Court Booking)");
            System.out.print("Choose mode (1-2): ");
            String mode = sc.nextLine().trim();

            transactionSeq++;
            String transId = "TXN-" + transactionSeq;
            
            if("1".equals(mode)){
                System.out.print("Enter Booking ID: ");
                String bookingId = sc.nextLine().trim();
                Booking booking = book.findBooking(bookingId);

                if(booking == null){
                    System.out.println("Error: Booking ID not Found");
                }
                if(booking.isCancelled()){
                    System.out.println("Error: Booking is cancelled");
                    return;
                }

                activeTransaction = new rentalTransaction(transId, booking);

                Item courtItem = cat.findByCode(booking.getFacilityCode());
                    if(courtItem != null){
                        activeTransaction.addItem(courtItem, booking.getDurationHours());
                }
                System.out.println("Transaction " + transId + "linked to " + booking.getBookingID());
            }else if ("2".equals(mode)){
                System.out.print("Enter name: ");
                String name = sc.nextLine().trim().toUpperCase();
                System.out.print("Enter Contact Number: ");
                String contactNo = sc.nextLine().trim();

                activeTransaction = new rentalTransaction(transId, name, contactNo);
                System.out.println("Transaction" + transId +"for walk in customer.");
            }else {
                System.out.println("Invalid Selection");
                return;
            }
        }
        boolean adding = true;
        while(adding){
            System.out.println("\nActive Transaction: " + activeTransaction.getTransactionId() + " (" + activeTransaction.getCustomerName() + ")");
        
        System.out.println("Enter Item Code to add");
        System.out.println("Type done once Finished");
        String code = sc.nextLine().trim().toUpperCase();

        if("DONE".equals(code)){
            adding = false;
            break;
        }

        Item item = cat.findByCode(code);
        if(item == null){
            System.out.println("Error: Unkown Item Code.");
            System.out.println("View Catalogue For item code");
            continue;
        }

        System.out.print("Enter Quantity of Item for: " + item.getItemName());
        try {
            int Quantity = Integer.parseInt(sc.nextLine().trim());
            activeTransaction.addItem(item, Quantity);
            System.out.println("Item Added: " + item.getItemName() + "x" + Quantity);
        } catch (NumberFormatException e) {
            System.out.println("Error: Quantity must be positive");
        }
    }
    activeTransaction.displayReceipt();

    }
    private static void BookingModule() {
        System.out.println("FACILITY BOOKING MODULE");
        System.out.println("1. Reserve Court / Facility");
        System.out.println("2. Cancel Existing Booking");
        System.out.println("3. View All Bookings");
        System.out.print("Select sub-option (1-3): ");
        String sub = sc.nextLine().trim();

        if("1".equals(sub)){
            try {
                System.out.print("Enter Name: ");
                String name = sc.nextLine().trim();
                System.out.print("Enter Contact Number");
                String number = sc.nextLine().trim();
                System.out.println("Enter Facility Code (Court - xx");
                String court = sc.nextLine().trim().toUpperCase();

                System.out.print("Enter Date of Booking");
                LocalDate date = LocalDate.parse(sc.nextLine().trim());

                System.out.print("Enter Time of Booking (24 Hour format HH:MM");
                LocalTime time = LocalTime.parse(sc.nextLine().trim());

                System.out.print("Enter Duration of Booking");
                int duration = Integer.parseInt(sc.nextLine().trim());


                System.out.print("Enter Amount of Pax");
                int pax = Integer.parseInt(sc.nextLine().trim());

                Booking booking = book.createBooking(name, number, court, date, time, duration, pax);
                System.out.println("Booking Succesfull -> " + booking.getBookingID());
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date or time format. Please use YYYY-MM-DD and HH:MM.");
            }catch(Exception e){
                System.out.println("Booking Refused: " + e.getMessage());
            }
        }else if ("2".equals(sub)) {
            System.out.print("Enter Booking ID to Cancel Booking (BK-XXXX)");
            String id = sc.nextLine().trim();
            if(book.cancelBooking(id)){
                System.out.println("Success: Booking " + id + "has been cancelled");
            }else{
                System.out.println("Error: Booking ID not found or already cancelled");
            }
        }else if ("3".equals(sub)) {
            book.displayBookings();
        }
    }
    private static void printMainMenu(){
         System.out.println("\n=================================================");
        System.out.println("           ACTIVEHUB MAIN CONTROL CONSOLE        ");
        System.out.println("=================================================");
        System.out.println(" 1. Facility Booking Module");
        System.out.println(" 2. View Rental Catalogue");
        System.out.println(" 3. Create / Modify Rental Transaction");
        System.out.println(" 4. Evaluate & Apply Promotion");
        System.out.println(" 5. Settle Payment & Print Final Bill");
        System.out.println(" 6. Daily Management Report");
        System.out.println(" 7. Save & Exit System");
        System.out.println("=================================================");
    }
}