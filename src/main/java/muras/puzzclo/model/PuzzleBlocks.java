/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.model;

import static muras.puzzclo.utils.ComponentSize.*;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import muras.puzzclo.utils.PuzzleBlockColor;

/**
 * パズル内のブロックの配置に関するクラス。<br />
 * DragAndDropで配置が変わった際には、 パズル消去の判定を行います。
 * 
 * @author muramatsu
 * 
 */
public final class PuzzleBlocks {

	// パズル用のテーブルモデル(PUZZLE_NUM_ROWS行分確保)
	// ブロックの配置を保持
	private final DefaultTableModel tableModel = new PuzzleTableModel(
			new String[PUZZLE_NUM_ROWS], 0);

	/**
	 * パズル用のテーブルモデルのゲッター
	 * 
	 * @return テーブルモデル
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * パズルテーブル上にブロックを配置する。
	 */
	public void arragePuzzleBlocks() {
		final ImageIcon[][] tmpBlocks = new ImageIcon[PUZZLE_NUM_ROWS][PUZZLE_NUM_COLS];

		for (ImageIcon[] blockRow : tmpBlocks) {
			// セルにアイコンを代入するために通常のfor文を使用
			for (int i = 0; i < blockRow.length; i++) {
				blockRow[i] = PuzzleBlockColor.getRandomColor().getBlock();
			}
		}

		// テーブルモデルにアイコンをセット
		for (ImageIcon[] blockRow : tmpBlocks) {
			tableModel.addRow(blockRow);
		}
	}
	
	/**
	 * 2つのブロックの配置を入れ替える。
	 * 
	 * @param srcRow 入れ替え元のセルの行
	 * @param srcCol 入れ替え元のセルの列
	 * @param dstRow 入れ替え先のセルの行
	 * @param dstCol 入れ替え先のセルの行
	 */
	public void swap(int srcRow, int srcCol, int dstRow, int dstCol) {

		boolean invalidSrcIndex = srcRow < 0 || PUZZLE_NUM_ROWS <= srcRow
				|| srcCol < 0 || PUZZLE_NUM_COLS <= srcCol;
		boolean invalidDstIndex = dstRow < 0 || PUZZLE_NUM_ROWS <= dstRow
				|| dstCol < 0 || PUZZLE_NUM_COLS <= dstCol;

		if (invalidSrcIndex || invalidDstIndex) {
			throw new IndexOutOfBoundsException("引数のセルが範囲外です。");
		}

		// 入れ替え
		Object srcCell = tableModel.getValueAt(srcRow, srcCol);
		Object dstCell = tableModel.getValueAt(dstRow, dstCol);
		tableModel.setValueAt(dstCell, srcRow, srcCol);
		tableModel.setValueAt(srcCell, dstRow, dstCol);
	}

	/**
	 * パズルのテーブルにオブジェクトを格納するための定義を行うクラス
	 * 
	 * @author muramatsu
	 * 
	 */
	private static class PuzzleTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		PuzzleTableModel(String[] columnNames, int rowNum) {
			super(columnNames, rowNum);
		}

		/**
		 * アイコンを表示するために、 格納されているオブジェクトの本当のクラスを返すようにする。
		 * 
		 * @return 格納されているオブジェクトのクラス
		 */
		@Override
		public Class<?> getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}

		/**
		 * テーブルを編集不可にする。
		 * 
		 * @return false
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}
