/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.event;

/**
 * 得点の変化に関するObserverインタフェース
 * 
 * @author muramatsu
 *
 */
public interface ScoreListener {
	/**
	 * スコアのモデルの状態変化を受けて、ビューを更新する。
	 */
	public void scoreChanged(ScoreChangeEvent e);
}
