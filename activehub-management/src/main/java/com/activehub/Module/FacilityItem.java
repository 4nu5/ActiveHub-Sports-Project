package com.activehub.Module;

public class FacilityItem extends Item{
    private final String surfaceType;
    private final int maxCapacity;

    public FacilityItem(String itemCode, String itemName, double rentalPrice, String surfaceType, int maxCapacity) {
        super(itemCode, itemName, rentalPrice, ItemCategory.FACILITY);
        if(surfaceType == null || surfaceType.trim().isEmpty()){
            throw new IllegalArgumentException("Surface Type Cannot Be Empty");
        }
        if(maxCapacity <= 0){
            throw new IllegalArgumentException("Capacity Cannot Be Less than 0");
        }
        this.surfaceType = surfaceType.trim();
        this.maxCapacity = maxCapacity;
    }

    public String getsurfaceType(){
        return surfaceType;
    }
    public int getmaxCapacity(){
        return maxCapacity;
    }
    @Override
    public String getDetails(){
        return String.format("Surface %s, Max Cap %d Pax", surfaceType, maxCapacity);
    }
}
