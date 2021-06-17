package com.mygdx.rpgame.dialog;

public interface ConversationGraphObserver {
    public static enum ConversationCommandEvent{
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        NONE
    }

    void onNotify(final ConversationGraph graph, ConversationCommandEvent event);
}
