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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import muras.puzzclo.model.PuzzleBlocks;

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

	// パズル内のブロックとその処理
	private final PuzzleBlocks puzzleBlocks = new PuzzleBlocks();

	// パズル用のテーブル
	private final JTable puzzleTable = createPuzzleTable();

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
		puzzleBlocks.arragePuzzleBlocks();
		setDragAndDrop();

		add(nameLabel);
		add(puzzleTable);
	}

	private JTable createPuzzleTable() {
		JTable puzzleTable = new PuzzleTable(puzzleBlocks.getTableModel());

		// サイズの設定
		puzzleTable
				.setPreferredSize(new Dimension(PUZZLE_SIZE_LENGTH, PUZZLE_SIZE_LENGTH));
		puzzleTable.setRowHeight(PUZZLE_SIZE_LENGTH / PUZZLE_CELLNUM_OF_SIDE);
		// 枠の色の設定
		puzzleTable.setBorder(new LineBorder(Color.DARK_GRAY));
		puzzleTable.setGridColor(Color.LIGHT_GRAY);

		return puzzleTable;
	}

	private void setDragAndDrop() {
		// セル単位で選択できるようにする (行・列単位での選択にならないようにする)
		puzzleTable.setCellSelectionEnabled(false);
		puzzleTable.setRowSelectionAllowed(false);

		PuzzleDragListener dragListener = new PuzzleDragListener();
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

		// ドラッグの時間制限用のタイマー
		private final Timer timer = new Timer();
		private TimerTask task;

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
			cancelTimerTask();
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

			final int x = e.getPoint().x;
			final int y = e.getPoint().y;

			boolean outOfPuzzleTable = x < 0 || x > PUZZLE_SIZE_LENGTH || y < 0
					|| y > PUZZLE_SIZE_LENGTH;
			if (outOfPuzzleTable) {
				setCellSelection(NOT_SELECTED, NOT_SELECTED);
				cancelTimerTask();
				return;
			}

			final int dstRow = getCellNumFromPositon(y);
			final int dstCol = getCellNumFromPositon(x);

			// パズルブロックを入れ替える
			puzzleBlocks.swap(selectedRow, selectedCol, dstRow, dstCol);
			// 入れ替わった先を選択状態にする
			setCellSelection(dstRow, dstCol);
		}

		/**
		 * 4秒後にセルの選択を解除するように、タイマーを起動する。
		 */
		private void deselectCellAfter4Sec() {
			task = new TimerTask() {
				@Override
				public void run() {
					setCellSelection(NOT_SELECTED, NOT_SELECTED);
				}
			};
			timer.schedule(task, 4000);
		}

		private void cancelTimerTask() {
			if (task != null) {
				task.cancel();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

}
