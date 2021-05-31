package com.mygdx.rpgame;

import com.badlogic.gdx.graphics.g2d.Batch;

public class TopWorldMap extends Map{
    private static String _mapPath = "maps/topworld.tmx";


    TopWorldMap() {
        super(MapFactory.MapType.TOWN, _mapPath);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {

    }
}
