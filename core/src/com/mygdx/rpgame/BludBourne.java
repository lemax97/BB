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

	public static enum ScreenType{
		MainMenu,
		MainGame,
		LoadGame,
		NewGame
	}

	public Screen getScreenType(ScreenType screenType){
		switch (screenType){
			case MainMenu:
				return _mainMenuScreen;
			case MainGame:
				return _mainGameScreen;
			case LoadGame:
				return _loadGameScreen;
			case NewGame:
				return _newGameScreen;
			default:
				return _mainMenuScreen;
		}
	}

	@Override
	public void create() {
		_mainGameScreen = new MainGameScreen(this);
		_mainMenuScreen = new MainMenuScreen(this);
		_loadGameScreen = new LoadGameScreen(this);
		_newGameScreen = new NewGameScreen(this);
		setScreen(_mainMenuScreen);
	}

	@Override
	public void dispose() {
		_mainGameScreen.dispose();
		_mainMenuScreen.dispose();
		_loadGameScreen.dispose();
		_newGameScreen.dispose();
	}
}
