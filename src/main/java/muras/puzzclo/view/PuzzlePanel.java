/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import static muras.puzzclo.utils.ComponentSizes.*;

/**
 * パズル部分のパネル
 * 
 * @author muramatsu
 * 
 */
class PuzzlePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// 名称部分のラベル
	private final JLabel nameLabel = new NameLabel("puzzle");
	// パズル用の6x6のテーブル
	private final JTable puzzleTable = createPuzzleTable();

	/**
	 * コンストラクタ
	 */
	PuzzlePanel() {
		add(nameLabel);
		add(puzzleTable);
	}

	private JTable createPuzzleTable() {
		final JTable puzzleTable = new JTable(PUZZLE_NUM_ROWS, PUZZLE_NUM_COLS);
		// サイズの設定
		puzzleTable
				.setPreferredSize(new Dimension(PUZZLE_WIDTH, PUZZLE_HEIGHT));
		puzzleTable.setRowHeight(PUZZLE_HEIGHT / PUZZLE_NUM_ROWS);
		// 枠の色の設定
		puzzleTable.setBorder(new LineBorder(Color.DARK_GRAY));
		puzzleTable.setGridColor(Color.LIGHT_GRAY);
		// 編集不可にする
		puzzleTable.setDefaultEditor(Object.class, null);

		return puzzleTable;
	}

}
