package com.mygdx.rpgame.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.rpgame.Utility;
import com.mygdx.rpgame.UI.StoreInventoryObserver.StoreInventoryEvent;

public class StoreInventoryUI extends Window implements InventorySlotObserver, StoreInventorySubject {

   private int _numStoreInventorySlots = 30;
   private int _lengthSlotRow = 10;
   private Table _inventorySlotTable;
   private Table _playerInventorySlotTable;
   private DragAndDrop _dragAndDrop;
   private Array<Actor> _inventoryActors;

   private static String STORE_INVENTORY = "Store_Inventory";
   private static String PLAYER_INVENTORY = "Player_Inventory";

   private final int _slotWidth = 52;
   private final int _slotHeight = 52;

   private InventorySlotTooltip _inventorySlotTooltip;

   private Label _sellTotalLabel;
   private Label _buyTotalLabel;
   private Label _playerTotalGP;

   private int _tradeInVal = 0;
   private int _fullValue = 0;
   private int _playerTotal = 0;

   private Button _sellButton;
   private Button _buyButton;
   public TextButton _closeButton;

   private Table _buttons;
   private Table _totalLabels;

   private Array<StoreInventoryObserver> _observers;

   private Json _json;

   private static String SELL = "SELL";
   private static String BUY = "BUY";
   private static String GP = "GP";
   private static String PLAYER_TOTAL = "Player Total";

    public StoreInventoryUI() {
        super("Store Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        _observers = new Array<StoreInventoryObserver>();
        _json = new Json();

        this.setFillParent(true);

        //create
        _dragAndDrop = new DragAndDrop();
        _inventoryActors = new Array<Actor>();
        _inventorySlotTable = new Table();
        _inventorySlotTable.setName(STORE_INVENTORY);

        _playerInventorySlotTable = new Table();
        _playerInventorySlotTable.setName(PLAYER_INVENTORY);
        _inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUSUI_SKIN);

        _sellButton = new TextButton(SELL, Utility.STATUSUI_SKIN, "inventory");
        disabelButton(_sellButton, true);

        _sellTotalLabel = new Label(SELL + " : " + _tradeInVal + GP, Utility.STATUSUI_SKIN);
        _sellTotalLabel.setAlignment(Align.center);
        _buyTotalLabel = new Label(BUY + " : " + _fullValue + GP, Utility.STATUSUI_SKIN);
        _buyTotalLabel.setAlignment(Align.center);

        _playerTotalGP = new Label(PLAYER_TOTAL + " : " + _playerTotal + GP, Utility.STATUSUI_SKIN);

        _buyButton = new TextButton(BUY, Utility.STATUSUI_SKIN, "inventory");
        disableButton(_buyButton, true);

        _closeButton = new TextButton("X", Utility.STATUSUI_SKIN);

        _buttons = new Table();
        _buttons.defaults().expand().fill();
        _buttons.add(_sellButton).padLeft(10).padRight(10);
        _buttons.add(_buyButton).padLeft(10).padRight(10);

        _totalLabels = new Table();
        _totalLabels.defaults().expand().fill();
        _totalLabels.add(_sellTotalLabel).padLeft(40);
        _totalLabels.add();
        _totalLabels.add(_buyTotalLabel).padRight(40);

        //layout
        for (int i = 1; i <= _numStoreInventorySlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(_inventorySlotTooltip));
            inventorySlot.addObserver(this);
            inventorySlot.setName(STORE_INVENTORY);

            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            _inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            if (i % _lengthSlotRow == 0){
                _inventorySlotTable.row();
            }
        }

        for (int i = 1; i <= InventoryUI._numSlots; i++) {

        }
    }

    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {

    }

    @Override
    public void addObserver(StoreInventoryObserver storeObserver) {

    }

    @Override
    public void removeObserver(StoreInventoryObserver storeObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(String value, StoreInventoryObserver.StoreInventoryEvent event) {

    }
}
