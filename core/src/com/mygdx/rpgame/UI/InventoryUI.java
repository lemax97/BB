package com.mygdx.rpgame.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.rpgame.InventoryItem.ItemTypeID;
import com.mygdx.rpgame.InventoryItem.ItemUseType;
import com.mygdx.rpgame.InventoryItemFactory;
import com.mygdx.rpgame.Utility;

public class InventoryUI extends Window {

    private int _numSlots = 50;
    private int _lengthSlotRow = 10;
    private Table _inventorySlotTable;
    private Table _playerSlotTable;
    private Table _equipSlots;

    private final int _slotWidth = 52;
    private final int _slotHeight = 52;

    public InventoryUI() {
        super("Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        //create
        _inventorySlotTable = new Table();
        _inventorySlotTable.setName("Inventory_Slot_Table");

        _playerSlotTable = new Table();
        _equipSlots = new Table();
        _equipSlots.setName("Equipment_Slot_Table");

        _equipSlots.defaults().space(10);

        InventorySlot headSlot = new InventorySlot(ItemUseType.ARMOR_HELMET.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_helmet")));

        InventorySlot leftArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_weapon")));

        InventorySlot rightArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                        ItemUseType.WEAPON_TWOHAND.getValue() |
                        ItemUseType.ARMOR_SHIELD.getValue() |
                        ItemUseType.WAND_ONEHAND.getValue() |
                        ItemUseType.WAND_TWOHAND.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_shield")));

        InventorySlot chestSlot = new InventorySlot(ItemUseType.ARMOR_CHEST.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_chest")));

        InventorySlot legsSlot = new InventorySlot(ItemUseType.ARMOR_FEET.getValue(),
                new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_boot")));

        _playerSlotTable.setBackground(new Image(
                new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))).getDrawable());

        //layout
        for (int i = 1; i <= _numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            _inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            if (i % _lengthSlotRow == 0){
                _inventorySlotTable.row();
            }
        }

        _equipSlots.add();
        _equipSlots.add(headSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add(leftArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(chestSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(rightArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add();
        _equipSlots.right().add(legsSlot).size(_slotWidth, _slotHeight);

        _playerSlotTable.add(_equipSlots);

        this.add(_playerSlotTable).padBottom(20).row();
        this.add(_inventorySlotTable).row();
        this.pack();
    }

    public Table getInventorySlotTable(){
        return _inventorySlotTable;
    }

    public Table getEquipSlotTable(){
        return _equipSlots;
    }

    public void populateInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems){
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < inventoryItems.size; i++) {
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            ItemTypeID itemTypeID = ItemTypeID.valueOf(itemLocation.getItemTypeAtLocation());
            InventorySlot inventorySlot = ((InventorySlot)cells.get(itemLocation.getLocationIndex()).getActor());
            inventorySlot.clearAllInventoryItems();

            for (int index = 0; index < itemLocation.getNumberItemsAtLocation(); index++) {
                inventorySlot.add(InventoryItemFactory.getInstace().getInventoryItem(itemTypeID));
            }
        }
    }

    public Array<InventoryItemLocation> getInventory(Table targetTable){
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot)cells.get(i).getActor());
            if (inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if (numItems > 0){
                items.add(new InventoryItemLocation(i,
                        inventorySlot.getTopInventoryItem().getItemTypeID().toString(), numItems));
            }
        }
        return items;
    }
}
