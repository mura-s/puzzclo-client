/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.event;

import muras.puzzclo.model.PuzzcloState.GameState;

/**
 * ゲーム全体の状態変化に関するイベント
 * 
 * @author muramatsu
 * 
 */
public class GameStateChangeEvent {
	private final GameState source;

	private final String message;

	public GameStateChangeEvent(GameState source, String message) {
		this.source = source;
		this.message = message;
	}

	public GameState getSource() {
		return source;
	}

	public String getMessage() {
		return message;
	}

}
