/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.utils;

import javax.swing.ImageIcon;

/**
 * パズル上で操作する色付きのブロックに関するクラス
 * 
 * @author muramatsu
 * 
 */
public enum PuzzleBlockColor {
	BLUE, GREEN, PINK, PURPLE, RED, YELLOW;

	/**
	 * 各色に対応するパズルのブロック(ImageIcon)を取得する。
	 * 
	 * @return 対応するパズルのブロック
	 */
	public ImageIcon getBlock() {
		// jarにしても画像を表示できるように、ClassLoaderで読み込む
		ClassLoader cl = this.getClass().getClassLoader();
		return new ImageIcon(cl.getResource(name().toLowerCase() + ".png"),
				name());
	}

	/**
	 * PuzzleBlockColorsの中からランダムな色を返す。
	 * 
	 * @return ランダムな色
	 */
	public static PuzzleBlockColor getRandomColor() {
		int rand = (int) (Math.random() * length());
		return values()[rand];
	}

	/**
	 * PuzzleBlockColorsの要素数を返す。
	 * 
	 * @return PuzzleBlockColorsの要素数
	 */
	public static int length() {
		return values().length;
	}

}
