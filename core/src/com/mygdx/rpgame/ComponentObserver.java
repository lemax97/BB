package com.mygdx.rpgame;

public interface ComponentObserver {
    public static enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION
    }

    void onNotify(final String value, ComponentEvent event);
}
