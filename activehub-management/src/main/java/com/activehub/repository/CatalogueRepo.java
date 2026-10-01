package com.activehub.repository;
import com.activehub.Module.*;
import java.util.*;

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

        addItem(new ServiceItem("S01", "Drink",3, ""));
        addItem(new ServiceItem("S02", "Food",6, ""));
        addItem(new ServiceItem("S03", "Cleaning",4, ""));
        addItem(new ServiceItem("S04", "",0, ""));
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
