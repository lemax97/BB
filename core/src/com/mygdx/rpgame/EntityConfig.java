package com.mygdx.rpgame;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

import com.mygdx.rpgame.Entity.AnimationType;

public class EntityConfig {
    Array<AnimationConfig> animationConfig;

    public Array<AnimationConfig> getAnimationConfig() {
        return animationConfig;
    }

    static public class AnimationConfig{
        private float frameDuration = 1.0f;
        private AnimationType animationType;
        private Array<GridPoint2> gridPoints;
        private Array<String> texturePaths;

        public AnimationConfig(){
            animationType = AnimationType.IDLE;
            texturePaths = new Array<String>();
            gridPoints = new Array<GridPoint2>();
        }

        public Array<String> getTexturePaths() {
            return texturePaths;
        }

        public Array<GridPoint2> getGridPoints(){
            return gridPoints;
        }

        public AnimationType getAnimationType() {
            return animationType;
        }

        public float getFrameDuration() {
            return frameDuration;
        }
    }



}
