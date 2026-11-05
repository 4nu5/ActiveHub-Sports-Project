package com.activehub.main;

import java.util.Scanner;

import com.activehub.Module.rentalTransaction;
import com.activehub.Service.BookingSystem;
import com.activehub.persistance.ServicePersistance;
import com.activehub.promotion.PromoCalculator;
import com.activehub.repository.CatalogueRepo;




public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final CatalogueRepo cat = new CatalogueRepo();
    private static final BookingSystem book = new BookingSystem();
    private static final PromoCalculator promo  = new PromoCalculator();
    private static final ReportingService report = new ReportingService();
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
        }
    }
}