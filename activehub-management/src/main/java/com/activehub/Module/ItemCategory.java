package com.activehub.Module;

public enum ItemCategory {
    FACILITY("Facility / Court"),
    EQUIPMENT("Sports Equipment"),
    SERVICE("Add-on Service");

    private final String displayName;

    private ItemCategory(String displayName) {
        this.displayName = null;
    }

    public String getdisplayName(){
        return displayName;
    }
}
