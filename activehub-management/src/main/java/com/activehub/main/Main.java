package com.activehub.main;

import com.activehub.repository.CatalogueRepo;


public class Main {
    public static void main(String[] args) {
        System.out.println("Active Hub Centre");
        CatalogueRepo cat = new CatalogueRepo();

        cat.displayCatalogue();
    }
}