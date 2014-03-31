/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client;

import javax.swing.JFrame;

import muras.puzzclo.client.view.PuzzcloFrame;

/**
 * クライアント起動用のメインクラス
 * 
 * @author muramatsu
 * 
 */
public class PuzzcloClient {

	public static void main(String[] args) {
		JFrame puzzcloFrame = new PuzzcloFrame();
		puzzcloFrame.setVisible(true);
	}

}
