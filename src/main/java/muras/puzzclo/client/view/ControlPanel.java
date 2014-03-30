/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.view;

import static muras.puzzclo.client.utils.ComponentSize.*;
import static muras.puzzclo.client.utils.PuzzcloMessages.*;

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

import muras.puzzclo.client.event.GameStateChangeEvent;
import muras.puzzclo.client.event.GameStateListener;
import muras.puzzclo.client.event.ScoreChangeEvent;
import muras.puzzclo.client.event.ScoreListener;
import muras.puzzclo.client.model.PuzzcloState;
import muras.puzzclo.client.model.PuzzcloState.GameState;
import muras.puzzclo.client.model.TotalScore;
import muras.puzzclo.client.socket.PuzzcloWebSocketClient;

/**
 * ユーザが操作を行うためのパネル
 * 
 * @author muramatsu
 * 
 */
class ControlPanel extends JPanel implements ScoreListener, GameStateListener {
	private static final long serialVersionUID = 1L;

	// メッセージ表示部
	private final MessagePanel messagePanel = new MessagePanel();
	// 入力部
	private final InputPanel inputPanel = new InputPanel();

	// ゲームの得点
	private final TotalScore totalScore;

	// ゲームの状態
	private final PuzzcloState puzzcloState;

	/**
	 * コンストラクタ
	 */
	ControlPanel(TotalScore totalScore, PuzzcloState puzzcloState) {
		this.totalScore = totalScore;
		totalScore.addScoreListener(this);

		this.puzzcloState = puzzcloState;
		puzzcloState.addGameStateListener(this);

		add(messagePanel);
		add(inputPanel);
	}

	@Override
	public void scoreChanged(ScoreChangeEvent e) {
		messagePanel.messageArea.append(e.getMessage());

		if (e.getSource().getMyScore() == TotalScore.MAX_SCORE) {
			puzzcloState.setGameState(GameState.GAME_CLEAR);
		}
	}

	@Override
	public void gameStateChanged(GameStateChangeEvent e) {
		switch (e.getSource()) {
		case INIT:
			if (puzzcloState.isPrevPlayTwoPerson()) {
				messagePanel.messageArea.append(DISCONNECTED_MESSAGE);
			}

			break;

		case WAIT_CONNECT:
			messagePanel.messageArea.append(getWelcomeMessage(e.getMyName()));

			break;

		case SELECT_OPPONENT:
			messagePanel.messageArea.append(getWelcomeMessage(e.getMyName()));

			if (e.getOpponentList() == null || e.getOpponentList().size() == 0) {
				messagePanel.messageArea.append(NO_OPPONENT_MESSAGE);
				return;
			}

			messagePanel.messageArea.append(getSelectOpponentMessage(e
					.getOpponentList()));

			break;

		default:
			// 何もしない
			break;
		}

		messagePanel.messageArea.append(e.getMessage());
	}

	JButton getSubmitButton() {
		return inputPanel.submitButton;
	}

	JTextField getInputField() {
		return inputPanel.inputField;
	}

	/**
	 * メッセージ出力用のパネル
	 * 
	 * @author muramatsu
	 * 
	 */
	private class MessagePanel extends JPanel {
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

			submitButton.addActionListener(new InputActionListener());
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

		private void createWebSocketClient() {
			new PuzzcloWebSocketClient(totalScore, puzzcloState);
		}

		private class InputActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = inputField.getText();

				if (text.equals("")) {
					return;
				}

				if (text.equals("q") || text.equals("Q")) {
					puzzcloState.setGameState(GameState.INIT);
				}

				switch (puzzcloState.getGameState()) {
				case INIT:
					if (text.equals("1")) {
						puzzcloState.setGameState(GameState.PLAY_ONE_PERSON);
					} else if (text.equals("2")) {
						puzzcloState
								.setGameState(GameState.INPUT_YOUR_NAME_AS_SERVER);
					} else if (text.equals("3")) {
						puzzcloState
								.setGameState(GameState.INPUT_YOUR_NAME_AS_CLIENT);
					}

					break;

				case INPUT_YOUR_NAME_AS_SERVER:
					createWebSocketClient();
					puzzcloState.setGameState(
							GameState.CONNECT_SERVER_AS_SERVER, text);

					break;

				case INPUT_YOUR_NAME_AS_CLIENT:
					createWebSocketClient();
					puzzcloState.setGameState(
							GameState.CONNECT_SERVER_AS_CLIENT, text);

					break;

				case SELECT_OPPONENT:

					break;

				default:
					// 何もしない
					break;
				}

				// テキストフィールドを空にする
				inputField.setText("");
			}

		}

	}

}
