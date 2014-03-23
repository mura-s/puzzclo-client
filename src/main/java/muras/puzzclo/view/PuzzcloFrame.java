/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import static muras.puzzclo.utils.ComponentSize.*;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * クライアントフレーム全体
 * 
 * @author muramatsu
 * 
 */
public final class PuzzcloFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final String name = "パズクロ！ (Puzzle & Clocks)";

	// 操作部のパネル
	private final JPanel controlPanel = new ControlPanel();
	// 時計部のパネル
	private final JPanel clockPanel = new ClockPanel();
	// パズル部のパネル
	private final JPanel puzzlePanel = new PuzzlePanel();

	/**
	 * コンストラクタ
	 */
	public PuzzcloFrame() {
		super();
		setTitle(name);
		setBounds(FRAME_XPOS, FRAME_YPOS, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);

		// レイアウト作成
		setLayout(new GridLayout(1, 2));
		// 左側
		add(puzzlePanel);
		// 右側(panelに載せる)
		JPanel rightPanel = new JPanel();
		add(rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(clockPanel);
		rightPanel.add(controlPanel);
	}

}