/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.model;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.event.GameStateChangeEvent;
import muras.puzzclo.event.GameStateListener;

import static muras.puzzclo.utils.PuzzcloMessages.*;

/**
 * アプリ全体の状態を表すクラス
 * 
 * @author muramatsu
 * 
 */
public final class PuzzcloState {
	/**
	 * ゲームの状態
	 * 
	 * @author muramatsu
	 * 
	 */
	public enum GameState {
		INIT(INIT_MESSAGE), DURING_GAME(GAME_START_MESSAGE), GAME_CLEAR(
				GAME_CLEAR_MESSAGE);

		private GameState(String message) {
			this.message = message;
		}

		private String message;
	}

	private GameState gameState;

	private final List<GameStateListener> listeners = new ArrayList<>();

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;

		notifyToListeners();
	}

	public void addGameStateListener(GameStateListener listener) {
		listeners.add(listener);
	}

	public void notifyToListeners() {
		for (GameStateListener listener : listeners) {
			listener.gameStateChanged(new GameStateChangeEvent(gameState,
					gameState.message));
		}
	}
}
