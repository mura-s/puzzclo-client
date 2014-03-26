/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import static muras.puzzclo.utils.ComponentSize.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
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

import muras.puzzclo.event.PuzzleListener;
import muras.puzzclo.model.CellState;
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

	// セルの選択状態
	private CellState cellState = new CellState();

	// パズル用のテーブル
	private final JTable puzzleTable = createPuzzleTable();

	/**
	 * コンストラクタ
	 */
	PuzzlePanel() {
		puzzleBlocks.initPuzzleBlocks();
		setDragAndDrop();

		add(nameLabel);
		add(puzzleTable);
	}

	private JTable createPuzzleTable() {
		JTable puzzleTable = new PuzzleTable(puzzleBlocks.getTableModel());

		// サイズの設定
		puzzleTable.setPreferredSize(new Dimension(PUZZLE_SIZE_LENGTH,
				PUZZLE_SIZE_LENGTH));
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

	/**
	 * パズルテーブル用のJTable
	 * 
	 * @author muramatsu
	 * 
	 */
	private class PuzzleTable extends JTable implements PuzzleListener {
		private static final long serialVersionUID = 1L;

		/**
		 * コンストラクタ。 自身をリスナー(オブザーバ)として登録する。
		 * 
		 * @param tableModel
		 *            テーブルモデル
		 */
		PuzzleTable(DefaultTableModel tableModel) {
			super(tableModel);

			puzzleBlocks.addPuzzleListener(this);
			cellState.addPuzzleListener(this);
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

			if (row == cellState.getSelectedRow()
					&& column == cellState.getSelectedCol()) {
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

		@Override
		public void puzzleChanged() {
			repaint();
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
		
		// パズルを消した得点
		private int score = 0;
		
		// ジャッジ中かどうか。ジャッジ中ならマウスを反応しなくする。
		private boolean judging = false;

		/**
		 * マウスが押されたら、その位置を設定し、選択状態にする。<br />
		 * また、4秒後にドラッグ状態を自動で解除する。
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (judging) {
				return;
			}
			
			final int row = puzzleTable.rowAtPoint(e.getPoint());
			final int col = puzzleTable.columnAtPoint(e.getPoint());

			cellState.changeSelectedState(row, col);

			deselectCellAfter4Sec();
		}

		/**
		 * マウスが離されたら、選択していない状態にする。<br />
		 * マウスを話してから、得点計算を行う。
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if (judging) {
				return;
			}
			
			finishDrag();

			// アニメーションのために、別スレッドで動かす
			Thread th = new Thread(new Runnable() {

				public void run() {
					judging = true;
					score = puzzleBlocks.judgeCombo();
					judging = false;
					
					System.out.println(score);
				}
			});
			th.start();

		}

		/**
		 * マウスが他のブロック上に移動したら、 そのブロックとドラッグ中のブロックを交換する。
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if (judging) {
				return;
			}
			
			// NOT_SELECTEDの場合は、ブロックを交換しないようにする
			if (!cellState.isSelected()) {
				return;
			}

			final Point p = e.getPoint();

			// パズル上から外れたら終了
			if (!isPosOnPuzzleTable(p.x, p.y)) {
				finishDrag();
				return;
			}

			final int dstRow = puzzleTable.rowAtPoint(p);
			final int dstCol = puzzleTable.columnAtPoint(p);

			// パズルブロックを入れ替える
			puzzleBlocks.swap(cellState.getSelectedRow(),
					cellState.getSelectedCol(), dstRow, dstCol);
			// 入れ替わった先を選択状態にする
			cellState.changeSelectedState(dstRow, dstCol);
		}

		/**
		 * 4秒後にセルの選択を解除するように、タイマーを起動する。
		 */
		private void deselectCellAfter4Sec() {
			task = new TimerTask() {
				@Override
				public void run() {
					finishDrag();
				}
			};
			timer.schedule(task, 4000);
		}

		private void cancelTimerTask() {
			if (task != null) {
				task.cancel();
			}
		}

		private void finishDrag() {
			cancelTimerTask();

			cellState.changeSelectedState(CellState.NOT_SELECTED_NUM,
					CellState.NOT_SELECTED_NUM);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

}
