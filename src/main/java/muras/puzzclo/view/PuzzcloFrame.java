/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static muras.puzzclo.utils.ComponentSizes.*;

/**
 * クライアントフレーム全体
 * 
 * @author muramatsu
 * 
 */
public final class PuzzcloFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final String name = "Puzzle and Clocks";

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

		BoxLayout boxlayout = new BoxLayout(this.getContentPane(),
				BoxLayout.Y_AXIS);
		setLayout(boxlayout);

		add(controlPanel);
		add(clockPanel);
		add(puzzlePanel);
	}

}