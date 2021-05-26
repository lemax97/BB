package com.mygdx.rpgame;

import com.badlogic.gdx.Screen;
import com.mygdx.rpgame.screens.MainGameScreen;
import com.badlogic.gdx.Game;

public class BludBourne extends Game {

	public static final MainGameScreen _mainGameScreen = new MainGameScreen();

	@Override
	public void create() {
		setScreen((Screen) _mainGameScreen);
	}

	@Override
	public void dispose() {
		_mainGameScreen.dispose();
	}
}
