package com.mygdx.rpgame.profile;

import com.badlogic.gdx.utils.Array;

public class ProfileSubject {

    private Array<ProfileObserver> _observers;

    public ProfileSubject(){
        _observers = new Array<ProfileObserver>();
    }

    protected void notify(final ProfileManager profileManager, ProfileObserver.ProfileEvent event){
        for (ProfileObserver observer: _observers) observer.onNotify(profileManager, event);
    }
}
