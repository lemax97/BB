package com.mygdx.rpgame.UI;

public class InventoryItemLocation {
    private int locationIndex;
    private String itemTypeAtLocation;
    private int numberItemsAtLocation;

    public InventoryItemLocation(int locationIndex, String itemTypeAtLocation, int numberItemsAtLocation) {
        this.locationIndex = locationIndex;
        this.itemTypeAtLocation = itemTypeAtLocation;
        this.numberItemsAtLocation = numberItemsAtLocation;
    }

    public String getItemTypeAtLocation() {
        return itemTypeAtLocation;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public int getNumberItemsAtLocation(){
        return numberItemsAtLocation;
    }
}
