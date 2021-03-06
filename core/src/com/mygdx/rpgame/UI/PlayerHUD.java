package com.mygdx.rpgame.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.rpgame.Component;
import com.mygdx.rpgame.ComponentObserver;
import com.mygdx.rpgame.Entity;
import com.mygdx.rpgame.EntityConfig;
import com.mygdx.rpgame.InventoryItem;
import com.mygdx.rpgame.InventoryItem.ItemTypeID;
import com.mygdx.rpgame.MapManager;
import com.mygdx.rpgame.Utility;
import com.mygdx.rpgame.dialog.ConversationGraph;
import com.mygdx.rpgame.dialog.ConversationGraphObserver;
import com.mygdx.rpgame.profile.ProfileManager;
import com.mygdx.rpgame.profile.ProfileObserver;
import com.mygdx.rpgame.quest.QuestGraph;

public class PlayerHUD implements Screen, ProfileObserver,ComponentObserver,ConversationGraphObserver,StoreInventoryObserver, StatusObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;
    private Entity _player;

    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private ConversationUI _conversationUI;
    private StoreInventoryUI _storeInventoryUI;
    private QuestUI _questUI;

    private Dialog _messageBoxUI;
    private Json _json;
    private MapManager _mapMgr;

    private static final String INVENTORY_FULL = "Your inventory is full!";

    public PlayerHUD(Camera camera, Entity player, MapManager mapMgr) {
        _camera = camera;
        _player = player;
        _mapMgr = mapMgr;
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);
        //_stage.setDebugAll(true);

        _json = new Json();
        _messageBoxUI = new Dialog("Message", Utility.STATUSUI_SKIN, "solidbackground"){
            {
                button("OK");
                text(INVENTORY_FULL);
            }

            @Override
            protected void result(final Object object){
                cancel();
                setVisible(false);
            }
        };
        _messageBoxUI.setVisible(false);
        _messageBoxUI.pack();
        _messageBoxUI.setPosition(_stage.getWidth() / 2 - _messageBoxUI.getWidth() / 2, _stage.getHeight() / 2 - _messageBoxUI.getHeight() / 2);

        _statusUI = new StatusUI();
        _statusUI.setVisible(true);
        _statusUI.setPosition(0, 0);

        _inventoryUI = new InventoryUI();
        _inventoryUI.setMovable(false);
        _inventoryUI.setVisible(false);
        _inventoryUI.setPosition(_stage.getWidth() / 2, 0);

        _conversationUI = new ConversationUI();
        _conversationUI.setMovable(true);
        _conversationUI.setVisible(false);
        _conversationUI.setPosition(_stage.getWidth() / 2, 0);
        _conversationUI.setWidth(_stage.getWidth() / 2);
        _conversationUI.setHeight(_stage.getHeight() / 2);

        _storeInventoryUI = new StoreInventoryUI();
        _storeInventoryUI.setMovable(false);
        _storeInventoryUI.setVisible(false);
        _storeInventoryUI.setPosition(0, 0);

        _questUI = new QuestUI();
        _questUI.setMovable(false);
        _questUI.setVisible(false);
        _questUI.setPosition(0, _stage.getHeight() / 2);
        _questUI.setWidth(_stage.getWidth());
        _questUI.setHeight(_stage.getHeight() / 2);

        _stage.addActor(_questUI);
        _stage.addActor(_storeInventoryUI);
        _stage.addActor(_inventoryUI);
        _stage.addActor(_conversationUI);
        _stage.addActor(_statusUI);
        _stage.addActor(_messageBoxUI);

        //add tooltips to the stage
        Array<Actor> actors = _inventoryUI.getInventoryActors();
        for(Actor actor : actors){
            _stage.addActor(actor);
        }

        Array<Actor> storeActors = _storeInventoryUI.getInventoryActors();
        for(Actor actor : storeActors ){
            _stage.addActor(actor);
        }

        //Observers
        ProfileManager.getInstance().addObserver(this);
        _player.registerObserver(this);
        _statusUI.addObserver(this);
        _storeInventoryUI.addObserver(this);

        //Listeners
        ImageButton inventoryButton = _statusUI.getInventoryButton();
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _inventoryUI.setVisible(_inventoryUI.isVisible() ? false : true);
            }
        });

        ImageButton questButton = _statusUI.getQuestButton();
        questButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _questUI.setVisible(_questUI.isVisible() ? false : true);
            }
        });

        _conversationUI.getCloseButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _conversationUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();
            }
        }
        );

        _storeInventoryUI.getCloseButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _storeInventoryUI.savePlayerInventory();
                _storeInventoryUI.cleanupStoreInventory();
                _storeInventoryUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();
            }
        }
        );
    }

    public Stage getStage() {
        return _stage;
    }

    public void updateEntityObservers(){
        _mapMgr.unregisterCurrentMapEntityObservers();

        _questUI.initQuests(_mapMgr);

        _mapMgr.registerCurrentMapEntityObservers(this);
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch(event){
            case PROFILE_LOADED:
                //if goldval is negative, this is our first save
                int goldVal = profileManager.getProperty("currentPlayerGP", Integer.class);
                boolean firstTime = goldVal<0?true:false;

                if( firstTime ){
                    //add default items if first time
                    Array<ItemTypeID> items = _player.getEntityConfig().getInventory();
                    Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                    for( int i = 0; i < items.size; i++){
                        itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1, InventoryUI.PLAYER_INVENTORY));
                    }
                    InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), itemLocations, _inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);
                    profileManager.setProperty("playerInventory", InventoryUI.getInventory(_inventoryUI.getInventorySlotTable()));
                }

                Array<InventoryItemLocation> inventory = profileManager.getProperty("playerInventory", Array.class);
                InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), inventory, _inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);

                Array<InventoryItemLocation> equipInventory = profileManager.getProperty("playerEquipInventory", Array.class);
                if (equipInventory != null && equipInventory.size > 0){
                    InventoryUI.populateInventory(_inventoryUI.getEquipSlotTable(), equipInventory, _inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);
                }

                Array<QuestGraph> quests = profileManager.getProperty("playerQuests", Array.class);
                _questUI.setQuests(quests);

                int xpMaxVal = profileManager.getProperty("currentPlayerXPMax", Integer.class);
                int xpVal = profileManager.getProperty("currentPlayerXP", Integer.class);

                //Check gold
                if (firstTime){
                    //start the player with some money
                    goldVal = 20;
                    xpMaxVal = 200;
                }

                //set the current max values first
                _statusUI.setXPValueMax(xpMaxVal);

                //then add in current values
                _statusUI.setGoldValue(goldVal);
                _statusUI.setXPValue(xpVal);

                break;
            case SAVING_PROFILE:
                profileManager.setProperty("playerQuests", _questUI.getQuests());
                profileManager.setProperty("playerInventory", InventoryUI.getInventory(_inventoryUI.getInventorySlotTable()));
                profileManager.setProperty("playerEquipInventory", InventoryUI.getInventory(_inventoryUI.getEquipSlotTable()));
                profileManager.setProperty("currentPlayerGP", _statusUI.getGoldValue() );
                profileManager.setProperty("currentPlayerXP", _statusUI.getXPValue() );
                profileManager.setProperty("currentPlayerXPMax", _statusUI.getXPValueMax() );
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(String value, ComponentEvent event) {
        switch(event) {
            case LOAD_CONVERSATION:
                EntityConfig config = _json.fromJson(EntityConfig.class, value);
                _conversationUI.loadConversation(config);
                _conversationUI.getCurrentConversationGraph().addObserver(this);
                break;
            case SHOW_CONVERSATION:
                EntityConfig configShow = _json.fromJson(EntityConfig.class, value);
                System.out.println("Show conversation for: " + configShow.getEntityID() + " current conversation ID: " + _conversationUI.getCurrentEntityID());
                if( configShow.getEntityID().equalsIgnoreCase(_conversationUI.getCurrentEntityID())) {
                    _conversationUI.setVisible(true);
                }
                break;
            case HIDE_CONVERSATION:
                EntityConfig configHide = _json.fromJson(EntityConfig.class, value);
                if (configHide.getEntityID().equalsIgnoreCase(_conversationUI.getCurrentEntityID())){
                    _conversationUI.setVisible(false);
                }
                break;
            case QUEST_LOCATION_DISCOVERED:
                String[] string = value.split(Component.MESSAGE_TOKEN);
                String questID = string[0];
                String questTaskID = string[1];

                _questUI.questTaskComplete(questID, questTaskID);
                updateEntityObservers();
            default:
                break;
        }
    }

    @Override
    public void onNotify(ConversationGraph graph, ConversationCommandEvent event) {
        switch(event) {
            case LOAD_STORE_INVENTORY:
                Entity selectedEntity = _mapMgr.getCurrentSelectedMapEntity();
                if( selectedEntity == null ){
                    break;
                }

                Array<InventoryItemLocation> inventory = InventoryUI.getInventory(_inventoryUI.getInventorySlotTable());
                _storeInventoryUI.loadPlayerInventory(inventory);

                Array<InventoryItem.ItemTypeID> items = selectedEntity.getEntityConfig().getInventory();
                Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                for (int i = 0; i < items.size; i++) {
                    itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1, InventoryUI.STORE_INVENTORY));
                }

                _storeInventoryUI.loadStoreInventory(itemLocations);

                _conversationUI.setVisible(false);
                _storeInventoryUI.toFront();
                _storeInventoryUI.setVisible(true);
                break;
            case EXIT_CONVERSATION:
                _conversationUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();
                break;
            case ACCEPT_QUEST:
                Entity currentlySelectedEntity = _mapMgr.getCurrentSelectedMapEntity();
                if ( currentlySelectedEntity == null){
                    break;
                }
                EntityConfig config = currentlySelectedEntity.getEntityConfig();

                QuestGraph questGraph = _questUI.loadQuest(config.getQuestConfigPath());

                if (questGraph != null){
                    //Update conversation dialog
                    config.setConversationConfigPath(QuestUI.RETURN_QUEST);
                    config.setCurrentQuestID(questGraph.getQuestID());
                    ProfileManager.getInstance().setProperty(config.getEntityID(), config);
                    updateEntityObservers();
                }

                _conversationUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();
                break;
            case RETURN_QUEST:
                Entity returnEntity = _mapMgr.getCurrentSelectedMapEntity();
                if( returnEntity == null ){
                    break;
                }
                EntityConfig configReturn = returnEntity.getEntityConfig();

                EntityConfig configReturnProperty = ProfileManager.getInstance().getProperty(configReturn.getEntityID(), EntityConfig.class);
                if (configReturnProperty == null) return;

                String questID = configReturnProperty.getCurrentQuestID();

                if (_questUI.isQuestReadyForReturn(questID)){
                    QuestGraph quest = _questUI.getQuestByID(questID);
                    _statusUI.addXPValue(quest.getXpReward());
                    _statusUI.addGoldValue(quest.getGoldReward());
                    _inventoryUI.removeQuestItemFromInventory(questID);
                    configReturnProperty.setConversationConfigPath(QuestUI.FINISHED_QUEST);
                    ProfileManager.getInstance().setProperty(configReturnProperty.getEntityID(), configReturnProperty);
                }

                _conversationUI.setVisible(false);
                _mapMgr.clearCurrentSelectedMapEntity();

                break;
            case ADD_ENTITY_TO_INVENTORY:
                Entity entity = _mapMgr.getCurrentSelectedMapEntity();
                if (entity == null){
                    break;
                }

                if ( _inventoryUI.doesInventoryHaveSpace() ){
                    _inventoryUI.addEntityToInventory(entity, entity.getEntityConfig().getCurrentQuestID());
                    _mapMgr.clearCurrentSelectedMapEntity();
                    _conversationUI.setVisible(false);
                    entity.unregisterObserver();
                    _mapMgr.removeMapQuestEntity(entity);
                    _questUI.updateQuests(_mapMgr);
                }else {
                    _mapMgr.clearCurrentSelectedMapEntity();
                    _conversationUI.setVisible(false);
                    _messageBoxUI.setVisible(true);
                }

                break;
            case NONE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(String value, StoreInventoryEvent event) {
        switch (event) {
            case PLAYER_GP_TOTAL_UPDATED:
                int val = Integer.valueOf(value);
                _statusUI.setGoldValue(val);
                break;
            case PLAYER_INVENTORY_UPDATED:
                Array<InventoryItemLocation> items = _json.fromJson(Array.class, value);
                InventoryUI.populateInventory(_inventoryUI.getInventorySlotTable(), items, _inventoryUI.getDragAndDrop(), InventoryUI.PLAYER_INVENTORY, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(int value, StatusEvent event) {
        switch (event){
            case UPDATED_GP:
                _storeInventoryUI.setPlayerGP(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        _stage.act(delta);
        _stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        _stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        _stage.dispose();
    }

}
