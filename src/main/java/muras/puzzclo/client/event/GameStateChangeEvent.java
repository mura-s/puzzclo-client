/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.event;

import muras.puzzclo.client.model.PuzzcloState.GameState;

/**
 * ゲーム全体の状態変化に関するイベント
 * 
 * @author muramatsu
 * 
 */
public class GameStateChangeEvent {
	private final GameState source;

	private final String message;
	
	private final String myName;

	public GameStateChangeEvent(GameState source, String myName, String message) {
		this.source = source;
		this.myName = myName;
		this.message = message;
	}

	public GameState getSource() {
		return source;
	}
	
	public String getMyName() {
		return myName;
	}

	public String getMessage() {
		return message;
	}

}
