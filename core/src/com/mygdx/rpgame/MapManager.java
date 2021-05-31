package com.mygdx.rpgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    private Camera _camera;
    private boolean _mapChanged = false;
    private Map _currentMap;
    private Entity _player;

    public MapManager(){
    }

    public void loadMap(MapFactory.MapType mapType){
        Map map = MapFactory.getMap(mapType);

        if (map == null){
            Gdx.app.debug(TAG, "Map does not exist! ");
            return;
        }

        _currentMap = map;
        _mapChanged = true;
        Gdx.app.debug(TAG, "Player Start: (" +
                _currentMap.getPlayerStart().x + "," + _currentMap.getPlayerStart().y + ")");
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        _currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer(){
        return _currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer(){
        return _currentMap.getPortalLayer();
    }

    public Vector2 getPlayerStartUnitScaled(){
        return _currentMap.getPlayerStartUnitScaled();
    }

    private  void setClosestStartPosition(final Vector2 position){

    }

    public TiledMap getCurrentMap(){

    }




    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta) {
    }

    public boolean hasMapChanged() {
        return false;
    }

    public TiledMap getCurrentTiledMap() {
    }

    public void setMapChanged(boolean b) {
    }

    public void setCamera(OrthographicCamera camera) {
    }

    public void setPlayer(Entity player) {
    }
}
