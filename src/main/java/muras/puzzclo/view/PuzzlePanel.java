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
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

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
	final JTable puzzleTable = createPuzzleTable();

	// セルが選択されていない状態
	private final int NOT_SELECTED = -1;
	// 選択中の行
	private int selectedRow = NOT_SELECTED;
	// 選択中の列
	private int selectedCol = NOT_SELECTED;

	/**
	 * コンストラクタ
	 */
	PuzzlePanel() {
		arragePuzzleBlocks();
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

	private void setDragAndDrop() {
		// セル単位で選択できるようにする (行・列単位での選択にならないようにする)
		puzzleTable.setCellSelectionEnabled(false);
		puzzleTable.setRowSelectionAllowed(false);

		final PuzzleDragListener dragListener = new PuzzleDragListener();
		// マウスクリックの設定
		puzzleTable.addMouseListener(dragListener);
		// ドラッグアンドドロップの設定
		puzzleTable.addMouseMotionListener(dragListener);
	}

	private void setCellSelection(int row, int col) {
		selectedRow = row;
		selectedCol = col;
		puzzleTable.repaint();
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
	 * パズルをドラッグアンドドロップで交換するためのリスナーです。
	 * 
	 * @author muramatsu
	 * 
	 */
	private class PuzzleDragListener extends MouseAdapter implements
			MouseMotionListener {

		/**
		 * マウスが押されたら、その位置を設定し、選択状態にする。<br />
		 * また、4秒後にドラッグ状態を自動で解除する。
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			setCellSelection(puzzleTable.rowAtPoint(e.getPoint()),
					puzzleTable.columnAtPoint(e.getPoint()));

			deselectCellAfter4Sec();
		}

		/**
		 * マウスが離されたら、選択していない状態にする。
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			setCellSelection(NOT_SELECTED, NOT_SELECTED);
		}

		/**
		 * マウスが他のブロック上に移動したら、 そのブロックとドラッグ中のブロックを交換する。
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			// NOT_SELECTEDの場合は、ブロックを交換しないようにする。
			if (selectedRow == NOT_SELECTED && selectedCol == NOT_SELECTED) {
				return;
			}

			int x = e.getPoint().x;
			int y = e.getPoint().y;

			boolean outOfPuzzleTable = x < 0 || x > PUZZLE_WIDTH || y < 0
					|| y > PUZZLE_HEIGHT;
			if (outOfPuzzleTable) {
				setCellSelection(NOT_SELECTED, NOT_SELECTED);
				e.consume();
				return;
			}

			int dstRow = getCellNumFromPositon(y);
			int dstCol = getCellNumFromPositon(x);

			// パズルブロックを入れ替える
			swap(selectedRow, selectedCol, dstRow, dstCol);
			// 入れ替わった先を選択状態にする
			setCellSelection(dstRow, dstCol);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		/**
		 * 4秒後にセルの選択を解除する。
		 */
		private void deselectCellAfter4Sec() {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					setCellSelection(NOT_SELECTED, NOT_SELECTED);
				}
			};
			timer.schedule(task, 4000);
		}

		private void swap(int srcRow, int srcCol, int dstRow, int dstCol) {

			boolean invalidSrcIndex = srcRow < 0 || PUZZLE_NUM_ROWS <= srcRow
					|| srcCol < 0 || PUZZLE_NUM_COLS <= srcCol;
			boolean invalidDstIndex = dstRow < 0 || PUZZLE_NUM_ROWS <= dstRow
					|| dstCol < 0 || PUZZLE_NUM_COLS <= dstCol;

			if (invalidSrcIndex || invalidDstIndex) {
				throw new IndexOutOfBoundsException("引数が範囲外です。");
			}

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
