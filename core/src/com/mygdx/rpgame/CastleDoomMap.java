package com.mygdx.rpgame;

import com.badlogic.gdx.graphics.g2d.Batch;

public class CastleDoomMap extends Map{
    private static String _mapPath = "maps/castle_of_doom.tmx";

    CastleDoomMap() {
        super(MapFactory.MapType.TOWN, _mapPath);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {

    }
}
