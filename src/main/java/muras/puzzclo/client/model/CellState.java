/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.model;

import static muras.puzzclo.client.utils.ComponentSize.*;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.client.event.PuzzleListener;

/**
 * セルの選択状態を表す。
 * 
 * @author muramatsu
 * 
 */
public class CellState {

	public static final int NOT_SELECTED_NUM = -1;

	private int selectedRow = NOT_SELECTED_NUM;
	private int selectedCol = NOT_SELECTED_NUM;

	private boolean selected = false;

	private final List<PuzzleListener> listeners = new ArrayList<>();

	/**
	 * 選択中の行番号を取得する。 未選択の場合は、SelectedState.NOT_SELECTEDを返す。
	 * 
	 * @return 選択中の行番号
	 */
	public int getSelectedRow() {
		return selectedRow;
	}

	/**
	 * 選択中の列番号を取得する。 未選択の場合は、SelectedState.NOT_SELECTEDを返す。
	 * 
	 * @return 選択中の列番号
	 */
	public int getSelectedCol() {
		return selectedCol;
	}

	/**
	 * セルの選択状態を取得する。
	 * 
	 * @return セルの選択状態
	 */
	public boolean isSelected() {
		return selected;
	}

	public void addPuzzleListener(PuzzleListener listener) {
		listeners.add(listener);
	}

	private void notifyToListeners() {
		for (PuzzleListener listener : listeners) {
			listener.puzzleChanged();
		}
	}

	/**
	 * セルの選択状態を変更する。 未選択の場合は、(SelectedState.NOT_SELECTED,
	 * SelectedState.NOT_SELECTED)を渡す。
	 * 
	 * @param row
	 *            変更後の行
	 * @param col
	 *            変更後の列
	 * @throws IllegalArgumentException
	 *             変更後のセルの位置が範囲外の場合
	 */
	public void changeSelectedState(int row, int col) {
		boolean notSelected = (row == NOT_SELECTED_NUM && col == NOT_SELECTED_NUM);
		if (!isCellOnPuzzleTable(row, col) && !notSelected) {
			throw new IllegalArgumentException("変更後のセルの位置が範囲外です。");
		}

		selectedRow = row;
		selectedCol = col;
		selected = !notSelected;

		notifyToListeners();
	}

}
