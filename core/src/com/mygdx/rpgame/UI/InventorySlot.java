package com.mygdx.rpgame.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.mygdx.rpgame.InventoryItem;
import com.mygdx.rpgame.Utility;

public class InventorySlot extends Stack {

    //All slots have this default image
    private Stack _defaultBackground;
    private Image _customBackgroundDecal;
    private Label _numItemsLabel;
    private int _numItemsVal = 0;
    private int _filterItemType;

    public InventorySlot() {
        _filterItemType = 0; //filter nothing
        _defaultBackground = new Stack();
        _customBackgroundDecal = new Image();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog")));

        _numItemsLabel = new Label(String.valueOf(_numItemsVal), Utility.STATUSUI_SKIN, "inventory-item-count");
        _numItemsLabel.setAlignment(Align.bottomRight);
        _numItemsLabel.setVisible(false);

        _defaultBackground.add(image);

        this.add(_defaultBackground);
        this.add(_numItemsLabel);
    }

    public InventorySlot(int filterItemType, Image customBackgroundDecal) {
        this();
        _filterItemType = filterItemType;
        _customBackgroundDecal = customBackgroundDecal;
        _defaultBackground.add(_customBackgroundDecal);
    }

    public void decrementItemCount() {
        _numItemsVal--;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if (_defaultBackground.getChildren().size == 1){
            _defaultBackground.add(_customBackgroundDecal);
        }
        checkVisibilityOfItemCount();
    }

    public void incrementItemCount(){
        _numItemsVal++;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if (_defaultBackground.getChildren().size > 1){
            _defaultBackground.getChildren().pop();
        }
        checkVisibilityOfItemCount();
    }

    @Override
    public void add(Actor actor) {
        super.add(actor);

        if (_numItemsLabel == null){
            return;
        }

        if (!actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel)){
            incrementItemCount();
        }
    }

    public void add(Array<Actor> array){
        for (Actor actor : array){
            super.add(actor);

            if (_numItemsLabel == null){
                return;
            }

            if (!actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel)){
                incrementItemCount();
            }
        }
    }

    public Array<Actor> getAllInventoryItems(){
        Array<Actor> items = new Array<>();
        if (hasItem()){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = arrayChildren.size - 2;
            for (int i = 0; i < numInventoryItems; i++) {
                items.add(arrayChildren.pop());
                decrementItemCount();
            }
        }
        return items;
    }

    public void clearAllInventoryItems(){
        if (hasItem()){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = getNumItems();
            for (int i = 0; i < numInventoryItems; i++) {
                arrayChildren.pop();
                decrementItemCount();
            }
        }
    }

    private void checkVisibilityOfItemCount(){
        if (_numItemsVal < 2){
            _numItemsLabel.setVisible(false);
        } else {
            _numItemsLabel.setVisible(true);
        }
    }

    public boolean hasItem() {
        if (hasChildren()){
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 2){
                return true;
            }
        }
        return false;
    }

    public int getNumItems() {
        if (hasChildren()){
            SnapshotArray<Actor> itmes = this.getChildren();
            return itmes.size - 2;
        }
        return 0;
    }

    public boolean doesAcceptItemUseType(int itemUseType){
        if (_filterItemType == 0){
            return true;
        }else {
            return ((_filterItemType & itemUseType) == itemUseType);
        }
    }

    public InventoryItem getTopInventoryItem() {
        InventoryItem actor = null;
        if (hasChildren()){
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 2){
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }

    public static void swapSlots(InventorySlot inventorySlotSource, InventorySlot inventorySlotTarget, InventoryItem dragActor) {
        //check if items can accept each other, otherwise, no swap
        if (!inventorySlotTarget.doesAcceptItemUseType(dragActor.getItemUseType()) ||
                !inventorySlotSource.doesAcceptItemUseType(inventorySlotTarget.getTopInventoryItem().getItemUseType())){
            inventorySlotSource.add(dragActor);
            return;
        }

        //swap
        Array<Actor> tempArray = inventorySlotSource.getAllInventoryItems();
        tempArray.add(dragActor);
        inventorySlotSource.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }
}
