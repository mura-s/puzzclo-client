/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.view;

import java.awt.Dimension;

import javax.swing.JLabel;

import static muras.puzzclo.client.utils.ComponentSize.*;

/**
 * パネルに配置する名称のラベル
 * 
 * @author muramatsu
 * 
 */
class NameLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param text
	 *            ラベルに表示する名称
	 */
	NameLabel(String text) {
		super(text);
		setPreferredSize(new Dimension(NAMELABEL_WIDTH, NAMELABEL_HEIGHT));
	}

}