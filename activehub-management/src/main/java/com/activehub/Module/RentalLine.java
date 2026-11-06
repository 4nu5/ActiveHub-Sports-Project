package com.activehub.Module;

import java.util.Objects;


/**
 * Demonstrates Composition/Aggregation: Represents an individual line item
 * inside a rental transaction, calculating line subtotals dynamically.
 */

public class RentalLine {
    private final Item item; 
    private int quantity;

    public RentalLine(Item item, int quantity){
        this.item = Objects.requireNonNull(item, "Item cannot be Null");

        if(quantity < 0){
            throw new IllegalArgumentException("Quantity Cannot be Less Than 0");
        }
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
    public void AddItem(int AddItem){
        if(AddItem <= 0){
            throw new IllegalArgumentException("Additional Items cannot be 0");
        }
        this.quantity += AddItem;
    }
    public double getTotalRentalItemPrice(){
        return item.getrentalPrice() * quantity;
    }
    @Override 
    public String toString(){
        return String.format("%-6s | %-24s | %3d x RM %6.2f | RM %7.2f",
                item.getItemCode(), item.getItemName(), quantity, item.getrentalPrice(), getTotalRentalItemPrice());
    }

}

