package com.activehub.repository;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.activehub.Module.Equipment;
import com.activehub.Module.FacilityItem;
import com.activehub.Module.Item;
import com.activehub.Module.ServiceItem;

public class CatalogueRepo {
    private final Map<String, Item> catalogue = new LinkedHashMap<>();

    public CatalogueRepo(){
        seedCatalogue();
    }
    private void seedCatalogue(){

        addItem(new FacilityItem("Court-01", "Badminton", 10,"Rubber", 4));
        addItem(new FacilityItem("Court-02", "Basketball", 12,"Hardwood", 10));
        addItem(new FacilityItem("Court-03", "Football", 14,"Grass", 22));


        addItem(new Equipment("E01", "Racket", "Good", 15));
        addItem(new Equipment("E02", "Shuttlecock", "Good", 3));
        addItem(new Equipment("E03", "Basketball", "Good", 7));
        addItem(new Equipment("E04", "Football", "Good", 7));
        addItem(new Equipment("E05", "Football boot", "Good", 7));

        addItem(new ServiceItem("S01", "Drink",3, "Per Bottle"));
        addItem(new ServiceItem("S02", "Food",6, "Per packet"));
        addItem(new ServiceItem("S03", "Cleaning",10, "Per booking"));
        addItem(new ServiceItem("S04", "Towel & Shower",12, "Per Booking"));
    }
    public void addItem(Item item){
        Objects.requireNonNull(item, "Item Cannot be Null");
        catalogue.put(item.getItemCode(), item);
    }
    public Item findByCode(String itemCode){
        if(itemCode == null) return null;
        return catalogue.get(itemCode.trim().toUpperCase());
    }
    public void displayCatalogue(){
        System.out.println("+--------+---------------------------+-------------------+-----------+---------------------------------+");
        System.out.printf("| %-6s | %-25s | %-17s | %-9s | %-31s |,\n","Code", "Item Name", "Category", "Price (RM)", "Specifications");

        System.out.println("+--------+---------------------------+-------------------+-----------+---------------------------------+");
        for(Item item : catalogue.values()){
            System.out.printf("| %-6s | %-25s | %-17s | %-9s | %-31s |\n", 
                item.getItemCode(),
                item.getItemName(),
                item.getCategory().getdisplayName(),
                item.getrentalPrice(),
                item.getDetails()
            );
            System.out.println("+--------+---------------------------+-------------------+-----------+---------------------------------+");
        }
    }
}
