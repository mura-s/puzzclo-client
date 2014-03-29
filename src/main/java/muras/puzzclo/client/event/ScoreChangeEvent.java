/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.event;

import muras.puzzclo.client.model.TotalScore;

/**
 * 得点の変化に関するイベント
 * 
 * @author muramatsu
 * 
 */
public class ScoreChangeEvent {
	private final TotalScore source;

	private final String message;

	public ScoreChangeEvent(TotalScore source, String message) {
		this.source = source;
		this.message = message;
	}

	public TotalScore getSource() {
		return source;
	}

	public String getMessage() {
		return message;
	}

}
