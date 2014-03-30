/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.event;

/**
 * ゲーム全体の状態の変化に関するObserverインタフェース
 * 
 * @author muramatsu
 * 
 */
public interface GameStateListener {

	/**
	 * ゲーム全体の状態変化を受けて、ビューを更新する。
	 */
	public void gameStateChanged(GameStateChangeEvent e);
}
