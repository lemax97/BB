package com.mygdx.rpgame.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class InventorySlotTooltip extends Window {

    private Skin _skin;
    private Label _description;

    public InventorySlotTooltip(final Skin skin) {
        super("", skin);
        this._skin = skin;
    }
}
