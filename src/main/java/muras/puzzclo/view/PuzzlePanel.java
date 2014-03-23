/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import muras.puzzclo.utils.PuzzleBlockColor;
import static muras.puzzclo.utils.ComponentSize.*;

/**
 * パズル部分のパネル
 * 
 * @author muramatsu
 * 
 */
class PuzzlePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// 名称部分のラベル
	private final JLabel nameLabel = new NameLabel("パズル");
	// パズルテーブル上のブロックの配置
	private final ImageIcon[][] puzzleBlocks = createPuzzleBlocks();
	// パズル用のテーブルモデル(PUZZLE_NUM_ROWS行分確保)
	private final DefaultTableModel tableModel = new PuzzleTableModel(
			new String[PUZZLE_NUM_ROWS], 0);
	// パズル用のテーブル
	private final JTable puzzleTable = createPuzzleTable();

	/**
	 * コンストラクタ
	 */
	PuzzlePanel() {
		arragePuzzleBlocks();

		add(nameLabel);
		add(puzzleTable);
	}

	private ImageIcon[][] createPuzzleBlocks() {
		final ImageIcon[][] puzzleBlocks = new ImageIcon[PUZZLE_NUM_ROWS][PUZZLE_NUM_COLS];
		for (ImageIcon[] blockRow : puzzleBlocks) {
			// セルにアイコンを代入するに通常のfor文を使用
			for (int i = 0; i < blockRow.length; i++) {
				blockRow[i] = PuzzleBlockColor.getRandomColor().getBlock();
			}
		}

		return puzzleBlocks;
	}

	private JTable createPuzzleTable() {
		final JTable puzzleTable = new JTable(tableModel);
		// サイズの設定
		puzzleTable
				.setPreferredSize(new Dimension(PUZZLE_WIDTH, PUZZLE_HEIGHT));
		puzzleTable.setRowHeight(PUZZLE_HEIGHT / PUZZLE_NUM_ROWS);
		// 枠の色の設定
		puzzleTable.setBorder(new LineBorder(Color.DARK_GRAY));
		puzzleTable.setGridColor(Color.LIGHT_GRAY);

		return puzzleTable;
	}

	private void arragePuzzleBlocks() {
		for (ImageIcon[] blockRow : puzzleBlocks) {
			tableModel.addRow(blockRow);
		}
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
		 * アイコンを表示するために、 格納されているオブジェクトの本当のクラスを返すようにする
		 * 
		 * @return 格納されているオブジェクトのクラス
		 */
		@Override
		public Class<?> getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}

		/**
		 * テーブルを編集不可にする
		 * 
		 * @return false
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}
