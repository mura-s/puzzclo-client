/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.utils;

/**
 * 各種Swingコンポーネントのサイズを定義するUtilクラス
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
	 * パズルのwidth
	 */
	public static final int PUZZLE_WIDTH = PANEL_WIDTH;
	/**
	 * パズルのheight
	 */
	public static final int PUZZLE_HEIGHT = PUZZLE_WIDTH;
	/**
	 * パズルの行の数
	 */
	public static final int PUZZLE_NUM_ROWS = 6;
	/**
	 * パズルの列の数
	 */
	public static final int PUZZLE_NUM_COLS = 6;


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

}
