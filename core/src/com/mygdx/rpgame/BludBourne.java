package com.mygdx.rpgame;

import com.badlogic.gdx.Screen;
import com.mygdx.rpgame.screens.MainGameScreen;
import com.badlogic.gdx.Game;

public class BludBourne extends Game {

	public static MainGameScreen _mainGameScreen;

	@Override
	public void create() {
		_mainGameScreen = new MainGameScreen(this);
		setScreen(_mainGameScreen);
	}

	@Override
	public void dispose() {
		_mainGameScreen.dispose();
	}
}
