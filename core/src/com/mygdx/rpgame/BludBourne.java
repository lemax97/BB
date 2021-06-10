package com.mygdx.rpgame;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Screen;
import com.mygdx.rpgame.screens.LoadGameScreen;
import com.mygdx.rpgame.screens.MainGameScreen;
import com.mygdx.rpgame.screens.MainMenuScreen;
import com.mygdx.rpgame.screens.NewGameScreen;

public class BludBourne extends Game {

	private static MainGameScreen _mainGameScreen;
	private static MainMenuScreen _mainMenuScreen;
	private static LoadGameScreen _loadGameScreen;
	private static NewGameScreen _newGameScreen;

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
