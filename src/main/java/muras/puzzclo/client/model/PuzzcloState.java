/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.model;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.client.event.GameStateChangeEvent;
import muras.puzzclo.client.event.GameStateListener;
import static muras.puzzclo.client.utils.PuzzcloMessages.*;

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
		/**
		 * 初期状態
		 */
		INIT(TITLE_MESSAGE + INIT_MESSAGE),

		/**
		 * 名前を入力中（接続を待つ側）
		 */
		INPUT_YOUR_NAME_AS_SERVER(INPUT_YOUR_NAME_MESSAGE),

		/**
		 * 名前を入力中（接続をする側）
		 */
		INPUT_YOUR_NAME_AS_CLIENT(INPUT_YOUR_NAME_MESSAGE),

		/**
		 * サーバに接続中
		 */
		CONNECT_SERVER(CONNECT_SERVER_MESSAGE),

		/**
		 * 対戦相手接続待ち
		 */
		WAIT_CONNECT(WAIT_CONNECT_MESSAGE),

		/**
		 * 対戦相手を選択中
		 */
		SELECT_OPPONENT(INPUT_OPPONENT_MESSAGE),

		/**
		 * 一人プレイ中
		 */
		PLAY_ONE_PERSON(GAME_START_MESSAGE),
		
		/**
		 * 二人プレイ開始
		 */
		START_PLAY_TWO_PERSON(GAME_START_MESSAGE),
		
		/**
		 * 自分のターン
		 */
		MY_TURN(MY_TURN_MESSAGE),
		
		/**
		 * 相手のターン
		 */
		OPPONENT_TURN(OPPONENT_TURN_MESSAGE),

		/**
		 * ゲームクリア後
		 */
		GAME_CLEAR(GAME_CLEAR_MESSAGE);

		private GameState(String message) {
			this.message = message;
		}

		private String message;
	}

	private GameState gameState;

	private String myName = "";

	private final List<GameStateListener> listeners = new ArrayList<>();

	public synchronized GameState getGameState() {
		return gameState;
	}

	public synchronized void setGameState(GameState gameState) {
		this.gameState = gameState;

		notifyToListeners();
	}

	public synchronized void setGameState(GameState gameState, String playerName) {
		this.gameState = gameState;
		this.myName = playerName;

		notifyToListeners();
	}

	public void addGameStateListener(GameStateListener listener) {
		listeners.add(listener);
	}

	private void notifyToListeners() {
		for (GameStateListener listener : listeners) {
			listener.gameStateChanged(new GameStateChangeEvent(gameState,
					myName, gameState.message));
		}
	}
}
