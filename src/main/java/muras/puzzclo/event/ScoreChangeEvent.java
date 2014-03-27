/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.event;

import muras.puzzclo.model.TotalScore;

/**
 * 得点の変化に関するイベント
 * 
 * @author muramatsu
 *
 */
public class ScoreChangeEvent {
	private final TotalScore source;
	
	public ScoreChangeEvent(TotalScore source) {
		this.source = source;
	}
	
	public TotalScore getSource() {
		return source;
	}
}
