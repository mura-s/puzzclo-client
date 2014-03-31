/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.model;

import static muras.puzzclo.client.utils.PuzzcloMessages.*;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.client.event.GameStateChangeEvent;
import muras.puzzclo.client.event.GameStateListener;

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
		 * 名前を入力中（接続を待つ側）
		 */
		INPUT_YOUR_NAME_AS_CLIENT(INPUT_YOUR_NAME_MESSAGE),

		/**
		 * サーバに接続中（接続を待つ側）
		 */
		CONNECT_SERVER_AS_SERVER(CONNECT_SERVER_MESSAGE),

		/**
		 * サーバに接続中（接続を待つ側）
		 */
		CONNECT_SERVER_AS_CLIENT(CONNECT_SERVER_MESSAGE),

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
		GAME_CLEAR(GAME_CLEAR_MESSAGE),
		
		/**
		 * 勝った時
		 */
		GAME_WIN(WIN_MESSAGE),
		
		/**
		 * 負けた時
		 */
		GAME_LOST(LOST_MESSAGE);

		private GameState(String message) {
			this.message = message;
		}

		private String message;
	}

	private GameState gameState;

	private String myName = "";

	private List<String> opponentList;

	private final List<GameStateListener> listeners = new ArrayList<>();

	public synchronized GameState getGameState() {
		return gameState;
	}

	/**
	 * ゲームの状態を設定する。 INITにする場合は、自分の名前も初期化される。
	 * 
	 * @param gameState
	 *            ゲームの状態
	 */
	public synchronized void setGameState(GameState gameState) {
		this.gameState = gameState;

		if (gameState == GameState.INIT) {
			myName = "";
		}

		notifyToListeners();
	}

	/**
	 * 2人対戦で、名前を設定するときに呼ぶ。
	 * 
	 * @param gameState
	 *            ゲームの状態
	 * @param myName
	 *            自分の名前
	 */
	public synchronized void setGameState(GameState gameState, String myName) {
		this.gameState = gameState;
		this.myName = myName;

		notifyToListeners();
	}

	/**
	 * 2人対戦で、名前と対戦相手リストを設定するときに呼ぶ。
	 * 
	 * @param gameState
	 *            ゲームの状態
	 * @param myName
	 *            自分の名前
	 * @param opponentList
	 *            対戦相手リスト
	 */
	public synchronized void setGameState(GameState gameState, String myName,
			List<String> opponentList) {
		this.gameState = gameState;
		this.myName = myName;
		this.opponentList = opponentList;

		notifyToListeners();
	}

	/**
	 * 追加のメッセージを頭につけるときに呼ぶ。
	 * 
	 * @param gameState
	 *            ゲームの状態
	 * @param message
	 *            メッセージ
	 */
	public synchronized void setGameStateWithMessage(GameState gameState,
			String message) {
		this.gameState = gameState;

		notifyToListenersWithMessage(message);
	}

	public String getMyName() {
		return myName;
	}

	public void addGameStateListener(GameStateListener listener) {
		listeners.add(listener);
	}

	private void notifyToListeners() {
		for (GameStateListener listener : listeners) {
			listener.gameStateChanged(new GameStateChangeEvent(gameState,
					myName, gameState.message, opponentList));
		}
	}

	private void notifyToListenersWithMessage(String message) {
		for (GameStateListener listener : listeners) {
			listener.gameStateChanged(new GameStateChangeEvent(gameState,
					myName, message + gameState.message, opponentList));
		}
	}
}
