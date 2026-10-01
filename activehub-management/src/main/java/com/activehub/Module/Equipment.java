package com.activehub.Module;

public class Equipment extends Item{
    private final String condition;

    public Equipment(String itemCode, String itemName, String condition, double rentalPrice){
        super(itemCode, itemName, rentalPrice, ItemCategory.EQUIPMENT);
        if(condition == null || condition.trim().isEmpty()){
            throw new IllegalArgumentException("Condition of an Item Cannot be empty");
        }
        this.condition = condition.trim();
    }
    public String getCondition(){
        return condition;
    }
    @Override
    public String getDetails(){
        return String.format("Condition %s", condition);
    }
}
