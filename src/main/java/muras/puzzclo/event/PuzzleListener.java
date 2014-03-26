/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.event;

/**
 * パズルの変化に関するObserverインタフェース
 * 
 * @author muramatsu
 *
 */
public interface PuzzleListener {
	/**
	 * パズルのモデルの状態変化を受けて、ビューを更新する。
	 */
	public void puzzleChanged();
}
