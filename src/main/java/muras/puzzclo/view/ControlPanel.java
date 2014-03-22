/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import static muras.puzzclo.utils.ComponentSizes.*;

/**
 * ユーザが操作を行うためのパネル
 * 
 * @author muramatsu
 * 
 */
class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// メッセージ表示部
	private final JPanel messagePanel = new MessagePanel();
	// 入力部
	private final JPanel inputPanel = new InputPanel();

	/**
	 * コンストラクタ
	 */
	ControlPanel() {
		add(messagePanel);
		add(inputPanel);
	}

	/**
	 * メッセージ出力用のパネル
	 * 
	 * @author muramatsu
	 * 
	 */
	private static class MessagePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// 名称部分のラベル
		private final JLabel nameLabel = new NameLabel("message");
		// メッセージ出力部分のパネル
		private final JTextArea messageArea = createMessageArea();

		/**
		 * コンストラクタ
		 */
		MessagePanel() {
			setLayout(new BorderLayout());
			add(BorderLayout.NORTH, nameLabel);
			add(BorderLayout.SOUTH, messageArea);
		}

		private JTextArea createMessageArea() {
			final JTextArea messageArea = new JTextArea();
			messageArea.setPreferredSize(new Dimension(MESSAGE_WIDTH,
					MESSAGE_HEIGHT));
			messageArea.setBorder(new LineBorder(Color.DARK_GRAY));
			return messageArea;
		}
	}

	/**
	 * 入力用のパネル
	 * 
	 * @author muramatsu
	 * 
	 */
	private static class InputPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// 名称部分のラベル
		private final JLabel nameLabel = new NameLabel("input");
		// 入力部分のフィールド
		private final JTextField inputField = createInputField();
		// 決定用ボタン
		private final JButton submitButton = createSubmitButton("ok");

		/**
		 * コンストラクタ
		 */
		InputPanel() {
			setLayout(new BorderLayout());
			add(BorderLayout.NORTH, nameLabel);
			add(BorderLayout.WEST, inputField);
			add(BorderLayout.EAST, submitButton);
		}

		private JTextField createInputField() {
			final JTextField inputField = new JTextField();
			inputField
					.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));

			return inputField;
		}

		private JButton createSubmitButton(String text) {
			final JButton submitButton = new JButton(text);
			submitButton.setPreferredSize(new Dimension(BUTTON_WIDTH,
					BUTTON_HEIGHT));
			return submitButton;
		}
	}

}
