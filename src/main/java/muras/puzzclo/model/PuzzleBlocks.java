/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.model;

import static muras.puzzclo.utils.ComponentSize.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import muras.puzzclo.event.PuzzleListener;
import muras.puzzclo.utils.PuzzleBlockColor;

/**
 * パズル内のブロックの配置に関するクラス。<br />
 * DragAndDropで配置が変わった際には、 パズル消去の判定を行います。
 * 
 * @author muramatsu
 * 
 */
public final class PuzzleBlocks implements PuzzleStateSubject {

	// パズル用のテーブルモデル(PUZZLE_NUM_ROWS行分確保)
	// ブロックの配置を保持
	private final DefaultTableModel tableModel = new PuzzleTableModel(
			new String[PUZZLE_CELLNUM_OF_SIDE], 0);

	private final List<PuzzleListener> listeners = new ArrayList<>();

	/**
	 * パズル用のテーブルモデルのゲッター
	 * 
	 * @return テーブルモデル
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	@Override
	public void addPuzzleListener(PuzzleListener listener) {
		listeners.add(listener);
	}

	@Override
	public void notifyToListeners() {
		for (PuzzleListener listener : listeners) {
			listener.puzzleChanged();
		}
	}

	/**
	 * パズルテーブル上にブロックを配置する。
	 */
	public void initPuzzleBlocks() {
		final ImageIcon[][] tmpBlocks = new ImageIcon[PUZZLE_CELLNUM_OF_SIDE][PUZZLE_CELLNUM_OF_SIDE];

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
	 * 入れ替え元と入れ替え先のセルが異なっている場合に、 2つのブロックの配置を入れ替える。
	 * 
	 * @param srcRow
	 *            入れ替え元のセルの行
	 * @param srcCol
	 *            入れ替え元のセルの列
	 * @param dstRow
	 *            入れ替え先のセルの行
	 * @param dstCol
	 *            入れ替え先のセルの行
	 */
	public void swap(int srcRow, int srcCol, int dstRow, int dstCol) {

		if (!isCellOnPuzzleTable(srcRow, srcCol)
				|| !isCellOnPuzzleTable(dstRow, dstCol)) {
			throw new IndexOutOfBoundsException("引数のセルが範囲外です。");
		}

		// 入れ替え元と入れ替え先のセルが異なっている場合に、入れ替え
		if ((srcRow != dstRow) || (srcCol != dstCol)) {
			Object srcCell = tableModel.getValueAt(srcRow, srcCol);
			Object dstCell = tableModel.getValueAt(dstRow, dstCol);
			tableModel.setValueAt(dstCell, srcRow, srcCol);
			tableModel.setValueAt(srcCell, dstRow, dstCol);
		}

		notifyToListeners();
	}

	/**
	 * パズルの配置から、消える部分を消去し、得点を計算する。<br />
	 * 
	 * 消えた部分には、上からパズルをドロップし、さらに消える部分があれば消す。
	 * この操作を繰り返し、消える部分がなくなった時点で、得点を合計し、returnする。<br />
	 * 
	 * ここで、消える部分は、3個以上の同じ色のブロックが隣り合っているところとする。
	 * 
	 * @return 得点
	 */
	public int judgeCombo() {

		// ブロック配置の取得
		final ImageIcon[][] blocks = getBlockArrangement();

		// 消えるブロックの位置
		// trueが消えるブロックを表す
		final boolean[][] delMap = new boolean[PUZZLE_CELLNUM_OF_SIDE][PUZZLE_CELLNUM_OF_SIDE];

		// 行方向の判定
		// 3個同じ色が続かないと消えないので、列は(PUZZLE_CELLNUM_OF_SIDE - 2)までで十分
		for (int i = 0; i < PUZZLE_CELLNUM_OF_SIDE; i++) {
			for (int j = 0; j < (PUZZLE_CELLNUM_OF_SIDE - 2); j++) {
				String thisColor = blocks[i][j].getDescription();
				String rightColor = blocks[i][j + 1].getDescription();
				String right2Color = blocks[i][j + 2].getDescription();

				if (thisColor.equals(rightColor)
						&& rightColor.equals(right2Color)) {
					delMap[i][j] = delMap[i][j + 1] = delMap[i][j + 2] = true;
				}
			}
		}

		// 列方向の判定
		// 行方向と同じ理由で、行は(PUZZLE_CELLNUM_OF_SIDE - 2)までで十分
		for (int j = 0; j < PUZZLE_CELLNUM_OF_SIDE; j++) {
			for (int i = 0; i < (PUZZLE_CELLNUM_OF_SIDE - 2); i++) {
				String thisColor = blocks[i][j].getDescription();
				String downColor = blocks[i + 1][j].getDescription();
				String down2Color = blocks[i + 2][j].getDescription();

				if (thisColor.equals(downColor) && downColor.equals(down2Color)) {
					delMap[i][j] = delMap[i + 1][j] = delMap[i + 2][j] = true;
				}
			}
		}

		// 得点を計算し、消えるブロックを消す
		int score = 0;

		for (int i = 0; i < PUZZLE_CELLNUM_OF_SIDE; i++) {
			for (int j = 0; j < PUZZLE_CELLNUM_OF_SIDE; j++) {
				if (delMap[i][j]) {
					score++;
					tableModel.setValueAt(null, i, j);
				}
			}
		}

		notifyToListeners();

		// 空いた部分を詰める
		

		// 新しいブロックを配置する。

		return score;
	}

	private ImageIcon[][] getBlockArrangement() {
		final ImageIcon[][] blocks = new ImageIcon[PUZZLE_CELLNUM_OF_SIDE][PUZZLE_CELLNUM_OF_SIDE];

		for (int i = 0; i < PUZZLE_CELLNUM_OF_SIDE; i++) {
			for (int j = 0; j < PUZZLE_CELLNUM_OF_SIDE; j++) {
				blocks[i][j] = (ImageIcon) tableModel.getValueAt(i, j);

				if (blocks[i][j] == null) {
					throw new AssertionError("blockが空のことはありません。");
				}
			}
		}

		return blocks;
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
			return ImageIcon.class;
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
