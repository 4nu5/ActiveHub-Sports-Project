package com.activehub.promotion;

import com.activehub.Module.ItemCategory;
import com.activehub.Module.rentalTransaction;

public class EquipmentBundlePromo implements Promotion {

    double EquipmentRateDiscount = 20.0;

      @Override
    public String getPromoCode(){
        return "Promo-C";
    }
    @Override 
    public String getPromoName(){
        return "Equipment Bundle Discount (RM 20)";
    }
    @Override 
    public boolean isEligible(rentalTransaction transaction){
        boolean hasFacility = transaction.getSubtotalByCategory(ItemCategory.FACILITY) > 0.0;
        boolean threeEquipment = transaction.getTotalEquipment() >= 3;

        return hasFacility && threeEquipment;
    }
    @Override 
    public double calculateDiscount(rentalTransaction transaction){
        if(!isEligible(transaction)){
            return 0.0;
        }
        return Math.min(EquipmentRateDiscount, transaction.getPreDisSubtotal());
    }
}
