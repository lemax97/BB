package com.mygdx.rpgame.UI;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.rpgame.Utility;

public class StatusUI extends Window implements StatusSubject {
    private Image _hpBar;
    private Image _mpBar;
    private Image _xpBar;

    private ImageButton _inventoryButton;
    Array<StatusObserver> _observers;

    //Attributes
    private int _levelVal = 1;
    private int _goldVal = -1;
    private int _hpVal = 50;
    private int _mpVal = 50;
    private int _xpVal = 0;

    private Label _hpValLabel;
    private Label _mpValLabel;
    private Label _xpValLabel;
    private Label _levelValLabel;
    private Label _goldValLabel;

    public StatusUI() {
        super("stats", Utility.STATUSUI_SKIN);

        _observers = new Array<StatusObserver>();

        //groups
        WidgetGroup group= new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        _hpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("HP_Bar"));
        Image bar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        _mpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("MP_Bar"));
        Image bar2 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        _xpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("XP_Bar"));
        Image bar3 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));

        //labels
        Label hpLabel = new Label(" hp:", Utility.STATUSUI_SKIN);
        _hpValLabel = new Label(String.valueOf(_hpVal), Utility.STATUSUI_SKIN);
        Label mpLabel = new Label(" mp:", Utility.STATUSUI_SKIN);
        _mpValLabel = new Label(String.valueOf(_mpVal), Utility.STATUSUI_SKIN);
        Label xpLabel = new Label(" xp:", Utility.STATUSUI_SKIN);
        _xpValLabel = new Label(String.valueOf(_xpVal), Utility.STATUSUI_SKIN);
        Label levelLabel = new Label(" lv:", Utility.STATUSUI_SKIN);
        _levelValLabel = new Label(String.valueOf(_levelVal), Utility.STATUSUI_SKIN);
        Label goldLabel = new Label(" gp:", Utility.STATUSUI_SKIN);
        _goldValLabel = new Label(String.valueOf(_goldVal), Utility.STATUSUI_SKIN);

        //buttons
        _inventoryButton= new ImageButton(Utility.STATUSUI_SKIN, "inventory-button");
        _inventoryButton.getImageCell().size(32, 32);

        //Align images
        _hpBar.setPosition(3, 6);
        _mpBar.setPosition(3, 6);
        _xpBar.setPosition(3, 6);

        //add to widget groups
        group.addActor(bar);
        group.addActor(_hpBar);
        group2.addActor(bar2);
        group2.addActor(_mpBar);
        group3.addActor(bar3);
        group3.addActor(_xpBar);

        //Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add();
        this.add();
        this.add(_inventoryButton).align(Align.right);
        this.row();

        this.add(group).size(bar.getWidth(), bar.getHeight());
        this.add(hpLabel);
        this.add(_hpValLabel).align(Align.left);
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight());
        this.add(mpLabel);
        this.add(_mpValLabel).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight());
        this.add(xpLabel);
        this.add(_xpValLabel).align(Align.left);
        this.row();

        this.add(levelLabel).align(Align.left);
        this.add(_levelValLabel).align(Align.left);
        this.row();
        this.add(goldLabel);
        this.add(_goldValLabel).align(Align.left);

        //this.debug();
        this.pack();
    }

    public ImageButton getInventoryButton() {
        return _inventoryButton;
    }

    public int getGoldValue(){
        return _goldVal;
    }

    public void setGoldValue(int goldValue){
        this._goldVal = goldValue;
        _goldValLabel.setText(String.valueOf(_goldVal));
        notify(goldValue, StatusObserver.StatusEvent.UPDATED_GP);
    }

    @Override
    public void addObserver(StatusObserver statusObserver) {
        _observers.add(statusObserver);
    }

    @Override
    public void removeObserver(StatusObserver statusObserver) {
        _observers.removeValue(statusObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for (StatusObserver observer: _observers){
            _observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(int value, StatusObserver.StatusEvent event) {
        for (StatusObserver observer: _observers){
            observer.onNotify(value, event);
        }
    }
}
