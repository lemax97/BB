package com.mygdx.rpgame.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.mygdx.rpgame.InventoryItem;

public class InventorySlot extends Stack {
    public InventorySlot() {
    }

    public InventorySlot(int i, Image inv_weapon) {
    }

    public static void swapSlots(InventorySlot sourceSlot, InventorySlot targetSlot, InventoryItem sourceActor) {
    }

    public InventoryItem getTopInventoryItem() {
        return null;
    }

    public void decrementItemCount() {
    }

    public void add(Actor dragActor) {
    }

    public void clearAllInventoryItems(){

    }

    public boolean doesAcceptItemUseType(int itemUseType){
        return false;
    }

    public boolean hasItem() {
        return false;
    }
}
