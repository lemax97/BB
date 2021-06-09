package com.mygdx.rpgame.profile;

public interface ProfileObserver {
    public static enum ProfileEvent{
        PROFILE_LOADED,
        SAVING_PROFILE
    }

    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}
