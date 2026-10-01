package com.activehub.Module;

public class ServiceItem extends Item{
    private final String billingUnit;

    public ServiceItem(String itemCode, String itemName, double rentalPrice, String billinUnit){
        super(itemCode, itemName, rentalPrice, ItemCategory.SERVICE);
        if(billinUnit == null || billinUnit.trim().isEmpty()){
            throw new IllegalArgumentException("Billing Unit cannot be blank");
        }
        this.billingUnit = billinUnit.trim();
    }
    public String getbilingUnit(){
        return billingUnit;
    }
    @Override
    public String getDetails(){
        return String.format("Unit %s",billingUnit);
    }
}
