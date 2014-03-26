/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.model;

import muras.puzzclo.event.PuzzleListener;

/**
 * PuzzleListener(オブザーバ)に対するサブジェクトインタフェース
 * 
 * @author muramatsu
 *
 */
public interface PuzzleStateSubject {

	public void addPuzzleListener(PuzzleListener listener);
	
	public void notifyToListeners();
}
