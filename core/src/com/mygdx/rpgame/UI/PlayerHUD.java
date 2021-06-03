package com.mygdx.rpgame.UI;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.rpgame.Entity;
import com.mygdx.rpgame.profile.ProfileObserver;

public class PlayerHUD implements Screen, ProfileObserver {

    private Stage _stage;
    private Viewport _viewport;
    private StatusUI _statusUI;
    private InventoryUI _inventoryUI;
    private Camera _camera;
    private Entity _player;

    public PlayerHUD(Camera camera, Entity player) {
        _camera = camera;
        _player = player;
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        _statusUI = new StatusUI();
        _inventoryUI = new InventoryUI();

        _stage.addActor(_statusUI);
        _stage.addActor(_inventoryUI);
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
        _stage.getViewport().update(width,height, true);
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

    public Stage getStage() {
        return _stage;
    }
}
