package com.mygdx.rpgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public abstract class PhysicsComponent implements Component{
    private static final String TAG = PhysicsComponent.class.getSimpleName();
    public abstract void update(Entity entity, MapManager mapMgr, float delta);

    protected Vector2 _nextEntityPosition;
    protected Vector2 _currentEntityPosition;
    protected Entity.Direction _currentDirection;
    protected Json _json;
    protected Vector2 _velocity;

    public Rectangle _boundingBox;
    protected BoundingBoxLocation _boundingBoxLocation;

    public static enum BoundingBoxLocation{
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    PhysicsComponent(){
        this._nextEntityPosition = new Vector2(0,0);
        this._currentEntityPosition = new Vector2(0, 0);
        this._velocity = new Vector2(2f, 2f);
        this._boundingBox = new Rectangle();
        this._json = new Json();
        _boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    }

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr){
        Array<Entity> entities = mapMgr.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for (Entity mapEntity: entities){
            //Check for testing against self
            if (mapEntity.equals(entity)){
                continue;
            }

            Rectangle targetRect = mapEntity.getCurrentBoundingBox();
            if (_boundingBox.overlaps(targetRect)){
                //Collision
                entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
                isCollisionWithMapEntities = true;
                break;
            }
        }
        return isCollisionWithMapEntities;
    }

    protected boolean isCollision(Entity entitySource, Entity entityTarget){
        boolean isCollisionWithMapEntities = false;
        if (entitySource.equals(entityTarget)){
            return false;
        }

        if (entitySource.getCurrentBoundingBox().overlaps(entityTarget.getCurrentBoundingBox())){
            //Collision
            entitySource.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
            isCollisionWithMapEntities = true;
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr){
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();

        if (mapCollisionLayer == null) return false;

        Rectangle rectangle = null;

        for (MapObject object: mapCollisionLayer.getObjects()){
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();
                if (_boundingBox.overlaps(rectangle)){
                    //Collision
                    entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }

        return false;
    }

    protected void setNextPositionToCurrent(Entity entity){
        this._currentEntityPosition.x = _nextEntityPosition.x;
        this._currentEntityPosition.y = _nextEntityPosition.y;

//        Gdx.app.debug(TAG, "SETTING Current Position " + entity.getEntityConfig().getEntityID() +
//                ": (" + _currentEntityPosition.x + "," + _currentEntityPosition.y + ")");
        entity.sendMessage(MESSAGE.CURRENT_POSITION, _json.toJson(_currentEntityPosition));
    }

    protected void calculateNextPosition(float deltaTime){
        if (_currentDirection == null) return;

        float testX = _currentEntityPosition.x;
        float testY = _currentEntityPosition.y;

        _velocity.scl(deltaTime);

        switch (_currentDirection){
            case LEFT:
                testX -= _velocity.x;
                break;
            case RIGHT:
                testX += _velocity.x;
                break;
            case UP:
                testY += _velocity.y;
                break;
            case DOWN:
                testY -= _velocity.y;
                break;
            default:
                break;
        }

        _nextEntityPosition.x = testX;
        _nextEntityPosition.y = testY;

        //velocity
        _velocity.scl(1 / deltaTime);
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced){
        //Update the current bounding box
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        //.8f for 20% (1 - .20)
        float widthReductionAmount = 1.0f - percentageWidthReduced;

        //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced;


    }
}
