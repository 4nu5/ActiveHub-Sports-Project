package com.activehub.Module;

import java.util.Objects;

public abstract class Item{

    private final String itemCode;
    private final ItemCategory category;
    private final String itemName;
    private double rentalPrice;
    
    public Item(String itemCode, String itemName, double rentalPrice, ItemCategory category){
        if(itemCode == null || itemCode.trim().isEmpty()){
            throw new IllegalArgumentException("Error Item Code cannot empty or blank");
        }
        if(itemName == null || itemName.trim().isEmpty()){
            throw new IllegalArgumentException("Error Item Name cannot empty or blank");
        }
        if(rentalPrice < 0.0){
            throw new IllegalArgumentException("Error item price cannot be negative");
        }

        this.category = Objects.requireNonNull(category, "Category Cannot be Null");
        this.itemCode = itemCode.trim().toUpperCase();
        this.itemName = itemName.trim();
        this.rentalPrice = rentalPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public String getItemName() {
        return itemName;
    }
    public double getrentalPrice(){
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice){
        if(rentalPrice < 0.0){
            throw new IllegalArgumentException("Rental Price cannot be negative: " + rentalPrice);
        }
        this.rentalPrice = rentalPrice;
    }

        public abstract String getDetails();

        @Override 
        public String toString(){
            return String.format("[%s] %-24s | %-16s | RM %6.2f",
                itemCode, itemName, category.getdisplayName(), rentalPrice);
        }
    }
