package com.mygdx.rpgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import com.mygdx.rpgame.BludBourne;
import com.mygdx.rpgame.MapManager;
import com.mygdx.rpgame.Entity;
import com.mygdx.rpgame.EntityFactory;
import com.mygdx.rpgame.Map;
import com.mygdx.rpgame.UI.PlayerHUD;
import com.mygdx.rpgame.profile.ProfileManager;
import com.mygdx.rpgame.Component;

public class MainGameScreen implements Screen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    public static enum GameState{
        RUNNING,
        PAUSED
    }
    private static GameState _gameState;

    private OrthogonalTiledMapRenderer _mapRenderer = null;
    private OrthographicCamera _camera = null;
    private OrthographicCamera _hudCamera = null;
    private static MapManager _mapMgr;
    private Json _json;
    private BludBourne _game;
    private InputMultiplexer _multiplexer;

    private static Entity _player;
    private static PlayerHUD _playerHUD;

    public MainGameScreen(BludBourne game){
        _game = game;
        _mapMgr = new MapManager();
        _json = new Json();

        _gameState = GameState.RUNNING;
        //_camera setup
        setupViewport(10, 10);

        //get the current size
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        _player = EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
        _mapMgr.setPlayer(_player);
        _mapMgr.setCamera(_camera);

        _hudCamera = new OrthographicCamera();
        _hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);

        _playerHUD = new PlayerHUD(_hudCamera, _player, _mapMgr);

        _multiplexer = new InputMultiplexer();
        _multiplexer.addProcessor(_playerHUD.getStage());
        _multiplexer.addProcessor(_player.getInputProcessor());
        Gdx.input.setInputProcessor(_multiplexer);

        //Gdx.app.debug(TAG, "UnitScale value is: " + _mapRenderer.getUnitScale());
    }

    @Override
    public void show() {
        _gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(_multiplexer);

        if (_mapRenderer == null){
            _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        }
    }

    @Override
    public void hide() {
        _gameState = GameState.PAUSED;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        if (_gameState == GameState.PAUSED){
            _player.updateInput(delta);
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _mapRenderer.setView(_camera);

//        _mapRenderer.getBatch().enableBlending();
//        _mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (_mapMgr.hasMapChanged()) {
            _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());
            _player.sendMessage(Component.MESSAGE.INIT_START_POSITION, _json.toJson(_mapMgr.getPlayerStartUnitScaled()));

            _camera.position.set(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y, 0f);
            _camera.update();

            _playerHUD.updateEntityObservers();

            _mapMgr.setMapChanged(false);
        }

        _mapRenderer.render();

        _mapMgr.updateCurrentMapEntities(_mapMgr, _mapRenderer.getBatch(), delta);

        _player.update(_mapMgr, _mapRenderer.getBatch(), delta);
        _playerHUD.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        setupViewport(10, 10);
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        _playerHUD.resize((int) VIEWPORT.physicalWidth, (int) VIEWPORT.physicalHeight);
    }

    @Override
    public void pause() {
        _gameState = GameState.PAUSED;
        ProfileManager.getInstance().saveProfile();
    }

    @Override
    public void resume() {
        _gameState = GameState.RUNNING;
        ProfileManager.getInstance().loadProfile();
    }

    public void dispose() {
        _player.unregisterObserver();
        _player.dispose();
        _mapRenderer.dispose();
    }

    public static void setGameState(GameState gameState){
        switch (gameState){
            case RUNNING:
                _gameState = GameState.RUNNING;
                break;
            case PAUSED:
                if (_gameState == GameState.PAUSED){
                    _gameState = GameState.RUNNING;
                    ProfileManager.getInstance().loadProfile();
                } else if (_gameState == GameState.RUNNING){
                    _gameState = GameState.PAUSED;
                    ProfileManager.getInstance().saveProfile();
                }
                break;
            default:
                _gameState = GameState.RUNNING;
                break;
        }
    }

    private void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        //aspect ration for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio){
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")");
    }
}
