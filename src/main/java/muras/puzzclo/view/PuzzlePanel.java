/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import static muras.puzzclo.utils.ComponentSize.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import muras.puzzclo.utils.PuzzleBlockColor;

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

	// パズルテーブル上のブロックの配置。配列の一次元目が行、二次元目が列。
	private final ImageIcon[][] puzzleBlocks = createPuzzleBlocks();
	// パズル用のテーブルモデル(PUZZLE_NUM_ROWS行分確保)
	private final DefaultTableModel tableModel = new PuzzleTableModel(
			new String[PUZZLE_NUM_ROWS], 0);
	// パズル用のテーブル
	private final JTable puzzleTable = createPuzzleTable();

	// 選択中の行
	private int selectedRow = -1;
	// 選択中の列
	private int selectedCol = -1;

	/**
	 * コンストラクタ
	 */
	PuzzlePanel() {
		arragePuzzleBlocks();
		setCellSelection();
		setDragAndDrop();

		add(nameLabel);
		add(puzzleTable);
	}

	private ImageIcon[][] createPuzzleBlocks() {
		final ImageIcon[][] puzzleBlocks = new ImageIcon[PUZZLE_NUM_ROWS][PUZZLE_NUM_COLS];

		for (ImageIcon[] blockRow : puzzleBlocks) {
			// セルにアイコンを代入するために通常のfor文を使用
			for (int i = 0; i < blockRow.length; i++) {
				blockRow[i] = PuzzleBlockColor.getRandomColor().getBlock();
			}
		}

		return puzzleBlocks;
	}

	private JTable createPuzzleTable() {
		final JTable puzzleTable = new PuzzleTable(tableModel);

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

	private void setCellSelection() {
		// puzzleTable.getSelectionModel().setSelectionMode(
		// ListSelectionModel.SINGLE_SELECTION);

		// セルを選択できるようにする (行・列単位での選択にならないようにする)
		puzzleTable.setCellSelectionEnabled(false);
		puzzleTable.setRowSelectionAllowed(false);

		// セルをクリックした時に、選択状態を反映
		puzzleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selectedRow = puzzleTable.rowAtPoint(e.getPoint());
				selectedCol = puzzleTable.columnAtPoint(e.getPoint());
				// prepareRendererでrepaintする
				puzzleTable.repaint();
			}
		});

	}

	private void setDragAndDrop() {
		puzzleTable.addMouseMotionListener(new PuzzleDragListener());

		// puzzleTable.setTransferHandler(new TransferHandler("text"));
		// puzzleTable.setDropMode(DropMode.INSERT_COLS);
		// puzzleTable.setDragEnabled(true);
	}

	/**
	 * パズルテーブル用のJTable
	 * 
	 * @author muramatsu
	 * 
	 */
	private class PuzzleTable extends JTable {
		private static final long serialVersionUID = 1L;

		/**
		 * コンストラクタ
		 * 
		 * @param tableModel
		 *            テーブルモデル
		 */
		PuzzleTable(DefaultTableModel tableModel) {
			super(tableModel);
		}

		/**
		 * セルの色の設定を行う。<br />
		 * 
		 * 選択されたセルの色を変更する。また、選択されていない行に関しては、マス目ごとに交互に色を設定する。
		 */
		@Override
		public Component prepareRenderer(TableCellRenderer tcr, int row,
				int column) {
			Component c = super.prepareRenderer(tcr, row, column);

			if (row == selectedRow && column == selectedCol) {
				// 選択されている場合
				c.setBackground(getSelectionBackground());
			} else if ((row + column) % 2 == 0) {
				// 行と列の和が偶数の場合
				c.setBackground(Color.LIGHT_GRAY);
			} else {
				// 行と列の和が奇数の場合
				c.setBackground(Color.WHITE);
			}

			return c;
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

	/**
	 * パズルをドラッグアンドドロップで交換するためのリスナーです。
	 * 
	 * @author muramatsu
	 * 
	 */
	private class PuzzleDragListener extends MouseMotionAdapter {

		/**
		 * マウスが他のブロック上に移動したら、 そのブロックとドラッグ中のブロックを交換する。
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			int dstRow = getCellNumFromPositon(e.getPoint().y);
			int dstCol = getCellNumFromPositon(e.getPoint().x);
			
			// パズルブロックを入れ替える
			swap(selectedRow, selectedCol, dstRow, dstCol);
			
			// 入れ替わった先を選択状態にする
			selectedRow = dstRow;
			selectedCol = dstCol;
			// prepareRendererでrepaintする
			puzzleTable.repaint();
		}

		private void swap(int srcRow, int srcCol, int dstRow, int dstCol) {
			// 保持しているパズルブロックを変更
			ImageIcon temp = puzzleBlocks[srcRow][srcCol];
			puzzleBlocks[srcRow][srcCol] = puzzleBlocks[dstRow][dstCol];
			puzzleBlocks[dstRow][dstCol] = temp;

			// 画面の表示を変更
			tableModel.setValueAt(puzzleBlocks[srcRow][srcCol], srcRow, srcCol);
			tableModel.setValueAt(puzzleBlocks[dstRow][dstCol], dstRow, dstCol);
		}
	}

}
