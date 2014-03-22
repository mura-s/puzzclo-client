/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo;

import javax.swing.JFrame;

import muras.puzzclo.view.PuzzcloFrame;

/**
 * クライアント起動用のメインクラス
 * 
 * @author muramatsu
 * 
 */
public class PuzzcloClient {

	public static void main(String[] args) {
		final JFrame puzzcloFrame = new PuzzcloFrame();
		puzzcloFrame.setVisible(true);
	}

}
