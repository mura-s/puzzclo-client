/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.view;

import static muras.puzzclo.client.utils.ComponentSize.*;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import muras.puzzclo.client.model.PuzzcloState;
import muras.puzzclo.client.model.TotalScore;
import muras.puzzclo.client.model.PuzzcloState.GameState;

/**
 * クライアントフレーム全体
 * 
 * @author muramatsu
 * 
 */
public final class PuzzcloFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final String title = "パズクロ！ (Puzzle & Clocks)";

	// ゲームの現在の得点
	private final TotalScore totalScore = new TotalScore();

	// ゲームの状態
	private final PuzzcloState puzzcloState = new PuzzcloState();

	// パズル部のパネル
	private final PuzzlePanel puzzlePanel = new PuzzlePanel(totalScore,
			puzzcloState);
	// 時計部のパネル
	private final ClockPanel clockPanel = new ClockPanel(totalScore);
	// 操作部のパネル
	private final ControlPanel controlPanel = new ControlPanel(totalScore,
			puzzcloState);

	/**
	 * コンストラクタ
	 */
	public PuzzcloFrame() {
		super();
		setTitle(title);
		setBounds(FRAME_XPOS, FRAME_YPOS, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		getRootPane().setDefaultButton(controlPanel.getSubmitButton());

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

		// フォーカスを入力欄に設定
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				controlPanel.getInputField().requestFocusInWindow();
			}
		});

		// 初期状態にする
		puzzcloState.setGameState(GameState.INIT);

	}

}