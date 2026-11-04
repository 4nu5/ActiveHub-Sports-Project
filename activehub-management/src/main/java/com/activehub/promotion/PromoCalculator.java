package com.activehub.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.activehub.Module.rentalTransaction;

public class PromoCalculator {
    private final List<Promotion> PromoAvailable = new ArrayList<>();

    public PromoCalculator(){
        PromoAvailable.add(new OutPeakPromo());
        PromoAvailable.add(new TeamBookingPromo());
        PromoAvailable.add(new EquipmentBundlePromo());
    }
    public static class resultPromo{
        private final Promotion promo;
        private final double discountAmount;
        
        public resultPromo(Promotion promo, double discountAmount){
            this.promo = promo;
            this.discountAmount = discountAmount;
        }

        public Promotion getPromo() {
            return promo;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }
        public resultPromo getBestPromo(rentalTransaction transaction){
            Promotion bestPromo = null;
            double maxDiscount = 0.0;

            if(promo.isEligible(transaction)){
                double discount = promo.calculateDiscount(transaction);
                
                if(discount > maxDiscount){
                    maxDiscount = discount;
                    bestPromo = promo;
                }else if(discount == maxDiscount && discount > 0.0 && bestPromo != null){
                    if(promo.getPromoCode().compareTo(bestPromo.getPromoCode()) < 0);
                        bestPromo = promo;
                }
            }
            return new resultPromo(bestPromo, maxDiscount);
        }
    }
               public List<Promotion> getPromotionsAvailable(){
            return Collections.unmodifiableList(PromoAvailable);
        }
}

