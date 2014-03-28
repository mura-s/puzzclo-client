/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import static muras.puzzclo.utils.ComponentSize.*;
import static muras.puzzclo.utils.PuzzcloMessages.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import muras.puzzclo.event.GameStateChangeEvent;
import muras.puzzclo.event.GameStateListener;
import muras.puzzclo.event.ScoreChangeEvent;
import muras.puzzclo.event.ScoreListener;
import muras.puzzclo.model.PuzzcloState;
import muras.puzzclo.model.PuzzcloState.GameState;
import muras.puzzclo.model.TotalScore;

/**
 * ユーザが操作を行うためのパネル
 * 
 * @author muramatsu
 * 
 */
class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// メッセージ表示部
	private final MessagePanel messagePanel = new MessagePanel();
	// 入力部
	private final JPanel inputPanel = new InputPanel();
	// ゲームの状態
	private final PuzzcloState puzzcloState;

	/**
	 * コンストラクタ
	 */
	ControlPanel(TotalScore totalScore, PuzzcloState puzzcloState) {
		totalScore.addScoreListener(messagePanel);

		this.puzzcloState = puzzcloState;
		puzzcloState.addGameStateListener(messagePanel);

		add(messagePanel);
		add(inputPanel);
	}

	/**
	 * メッセージ出力用のパネル
	 * 
	 * @author muramatsu
	 * 
	 */
	private static class MessagePanel extends JPanel implements ScoreListener,
			GameStateListener {
		private static final long serialVersionUID = 1L;

		// 名称部分のラベル
		private final JLabel nameLabel = new NameLabel("メッセージ");
		// メッセージ出力部分のパネル
		private final JTextArea messageArea = createMessageArea();

		/**
		 * コンストラクタ
		 */
		MessagePanel() {
			setLayout(new BorderLayout());
			add(BorderLayout.NORTH, nameLabel);

			// スクロールバーをつける
			JScrollPane sp = new JScrollPane(messageArea,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sp.setPreferredSize(new Dimension(MESSAGE_WIDTH, MESSAGE_HEIGHT));

			add(BorderLayout.SOUTH, sp);
		}

		private JTextArea createMessageArea() {
			JTextArea messageArea = new JTextArea() {
				private static final long serialVersionUID = 1L;

				// 自動スクロールの設定
				@Override
				public void append(String str) {
					super.append(str);
					setCaretPosition(getDocument().getLength());
				}
			};

			messageArea.setBorder(new LineBorder(Color.DARK_GRAY));
			messageArea.setEditable(false);
			return messageArea;
		}

		public void scoreChanged(ScoreChangeEvent e) {
			messageArea.append(e.getMessage());
		}

		public void gameStateChanged(GameStateChangeEvent e) {
			if (e.getSource() == GameState.INIT) {
				messageArea.append(TITLE_MESSAGE);
			}

			messageArea.append(e.getMessage());
		}
	}

	/**
	 * 入力用のパネル
	 * 
	 * @author muramatsu
	 * 
	 */
	private class InputPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// 名称部分のラベル
		private final JLabel nameLabel = new NameLabel("入力");
		// 入力部分のフィールド
		private final JTextField inputField = createInputField();
		// 決定用ボタン
		private final JButton submitButton = createSubmitButton("send");

		/**
		 * コンストラクタ
		 */
		InputPanel() {
			setLayout(new BorderLayout());
			add(BorderLayout.NORTH, nameLabel);
			add(BorderLayout.WEST, inputField);
			add(BorderLayout.EAST, submitButton);
			
			submitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = inputField.getText();
					
					if (text.equals("q") || text.equals("Q")) {
						puzzcloState.setGameState(GameState.INIT);
					}
					
					switch (puzzcloState.getGameState()) {
					case INIT:
						if (text.equals("1")) {
							puzzcloState.setGameState(GameState.DURING_GAME);
						}
						break;
					
					default:
						throw new AssertionError("ここには到達しない");
					}
					
					// テキストフィールドを空にする
					inputField.setText("");
				}
			});
		}

		private JTextField createInputField() {
			JTextField inputField = new JTextField();
			inputField
					.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));

			return inputField;
		}

		private JButton createSubmitButton(String text) {
			JButton submitButton = new JButton(text);
			submitButton.setPreferredSize(new Dimension(BUTTON_WIDTH,
					BUTTON_HEIGHT));
			return submitButton;
		}
	}

}
