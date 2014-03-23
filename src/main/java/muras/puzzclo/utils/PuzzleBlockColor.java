/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.utils;

import javax.swing.ImageIcon;

/**
 * パズル上で操作する色付きのブロックに関するクラス
 * 
 * @author muramatsu
 * 
 */
public enum PuzzleBlockColor {
	BLUE(new ImageIcon("./img/blue.png")),
	GREEN(new ImageIcon("./img/green.png")),
	PINK(new ImageIcon("./img/pink.png")),
	PURPLE(new ImageIcon("./img/purple.png")),
	RED(new ImageIcon("./img/red.png")),
	YELLOW(new ImageIcon("./img/yellow.png"));
	
	private PuzzleBlockColor(ImageIcon block) {
		this.block = block;
	}
	
	// パズルのブロックのアイコン
	private ImageIcon block;
	
	/**
	 * 各色に対応するパズルのブロック(ImageIcon)を取得する
	 * 
	 * @return 対応するパズルのブロック
	 */
	public ImageIcon getBlock() {
		return block;
	}
	
	/**
	 * PuzzleBlockColorsの中からランダムな色を返す
	 * 
	 * @return ランダムな色
	 */
	public static PuzzleBlockColor getRandomColor() {
		int rand = (int) (Math.random() * length());
		return values()[rand];
	}

	/**
	 * PuzzleBlockColorsの要素数を返す
	 * 
	 * @return PuzzleBlockColorsの要素数
	 */
	public static int length() {
		return values().length;
	}

}
