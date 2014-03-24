/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.utils;

/**
 * 各種Swingコンポーネントのサイズや位置を定義するUtilクラス
 * 
 * @author muramatsu
 * 
 */
public class ComponentSize {
	/**
	 * フレームのx座標
	 */
	public static final int FRAME_XPOS = 0;
	/**
	 * フレームのy座標
	 */
	public static final int FRAME_YPOS = 0;
	/**
	 * フレームのwidth
	 */
	public static final int FRAME_WIDTH = 880;
	/**
	 * フレームのheight
	 */
	public static final int FRAME_HEIGHT = 470;

	/**
	 * パネルのwidth
	 */
	public static final int PANEL_WIDTH = 402;

	/**
	 * パズルの辺の長さ。 <br />
	 * パズルの幅と高さは等しい。また、6で割り切れる数にする。
	 */
	public static final int PUZZLE_SIZE_LENGTH = PANEL_WIDTH;

	/**
	 * パズルの行の数
	 */
	public static final int PUZZLE_CELLNUM_OF_SIDE = 6;

	/**
	 * 時計のwidth
	 */
	public static final int CLOCK_WIDTH = PANEL_WIDTH;
	/**
	 * 時計のheight
	 */
	public static final int CLOCK_HEIGHT = 120;
	/**
	 * 時計のfontsize
	 */
	public static final int CLOCK_FONTSIZE = 70;

	/**
	 * メッセージエリアのwidth
	 */
	public static final int MESSAGE_WIDTH = PANEL_WIDTH;
	/**
	 * メッセージエリアのheight
	 */
	public static final int MESSAGE_HEIGHT = 160;

	/**
	 * 入力用フィールドのwidth
	 */
	public static final int INPUT_WIDTH = 330;
	/**
	 * 入力用フィールドのheight
	 */
	public static final int INPUT_HEIGHT = 30;

	/**
	 * 決定用ボタンのwidth
	 */
	public static final int BUTTON_WIDTH = 70;
	/**
	 * 決定用ボタンのheight
	 */
	public static final int BUTTON_HEIGHT = INPUT_HEIGHT;

	/**
	 * Nameラベルのwidth
	 */
	public static final int NAMELABEL_WIDTH = PANEL_WIDTH;
	/**
	 * Nameラベルのheight
	 */
	public static final int NAMELABEL_HEIGHT = 15;

	/**
	 * オブジェクト化を禁止
	 */
	private ComponentSize() {
	}

	/**
	 * パズル上の座標から、セルの位置を取得する。
	 * 
	 * @param pos
	 *            パズル上のx座標またはy座標
	 * @return 入力がx座標: 列番号<br />
	 *         入力がy座標: 行番号
	 * @throws IllegalArgumentException
	 *             posがパズルの外の場合 (pos < 0 || PUZZLE_WIDTH < pos)
	 */
	public static int getCellNumFromPositon(int pos) {
		if (pos < 0 || PUZZLE_SIZE_LENGTH < pos) {
			throw new IllegalArgumentException("座標が0以上" + PUZZLE_SIZE_LENGTH
					+ "以下ではありません。渡された座標: " + pos);
		}

		int cellSize = PUZZLE_SIZE_LENGTH / PUZZLE_CELLNUM_OF_SIDE;
		// 境界値も範囲内にする
		int cellNum = (pos == PUZZLE_SIZE_LENGTH) ? (PUZZLE_CELLNUM_OF_SIDE - 1)
				: (pos / cellSize);

		return cellNum;
	}
}
