/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.utils;

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
	 * パズルの一辺のセルの数
	 */
	public static final int PUZZLE_CELLNUM_OF_SIDE = 6;

	/**
	 * 時計のwidth
	 */
	public static final int CLOCK_WIDTH = PANEL_WIDTH;
	/**
	 * 時計のheight
	 */
	public static final int CLOCK_HEIGHT = 80;
	/**
	 * 時計のfontsize
	 */
	public static final int CLOCK_FONTSIZE = 90;

	/**
	 * メッセージエリアのwidth
	 */
	public static final int MESSAGE_WIDTH = PANEL_WIDTH;
	/**
	 * メッセージエリアのheight
	 */
	public static final int MESSAGE_HEIGHT = 200;

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
	 * x座標とy座標の位置のセルがパズルテーブル上かどうかを調べる。<br />
	 * 最大の境界値は範囲外とする。
	 * 
	 * @param x x座標
	 * @param y y座標
	 * @return true: テーブル上の場合、false: テーブル外の場合
	 */
	public static boolean isPosOnPuzzleTable(int x, int y) {
		if (x < 0 || PUZZLE_SIZE_LENGTH <= x ||
				y < 0 || PUZZLE_SIZE_LENGTH <= y) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * rowとcolの位置のセルがパズルテーブル上かどうかを調べる。
	 * 最大の境界値は範囲外とする。
	 * 
	 * @param row 行
	 * @param col 列
	 * @return true: テーブル上の場合、false: テーブル外の場合
	 */
	public static boolean isCellOnPuzzleTable(int row, int col) {
		if (row < 0 || PUZZLE_CELLNUM_OF_SIDE <= row ||
				col < 0 || PUZZLE_CELLNUM_OF_SIDE <= col) {
			return false;
		}
		
		return true;
	}
	
}
