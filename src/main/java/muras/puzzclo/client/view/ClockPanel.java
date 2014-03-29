/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.view;

import static muras.puzzclo.client.utils.ComponentSize.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import muras.puzzclo.client.model.TotalScore;

/**
 * 時計部分のパネル
 * 
 * @author muramatsu
 * 
 */
class ClockPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// 名称部分のラベル
	private final JLabel nameLabel = new NameLabel("時計");

	// ゲームの現在の得点
	private final TotalScore totalScore;

	// 時計部分のパネル
	private final Clock clock = createClock();

	/**
	 * コンストラクタ
	 */
	ClockPanel(TotalScore totalScore) {
		this.totalScore = totalScore;

		add(nameLabel);
		add(clock);
	}

	/**
	 * 時計を作成し、時刻表示のスレッドを動かし始める。
	 * 
	 * @return
	 */
	private Clock createClock() {
		final Clock clock = new Clock();
		clock.setPreferredSize(new Dimension(CLOCK_WIDTH, CLOCK_HEIGHT));
		clock.setBorder(new LineBorder(Color.DARK_GRAY));
		clock.setBackground(Color.WHITE);

		new Thread(clock).start();

		return clock;
	}

	/**
	 * 実際に時計を定義するクラス
	 * 
	 * @author muramatsu
	 * 
	 */
	private class Clock extends JPanel implements Runnable {
		private static final long serialVersionUID = 1L;

		String time;
		final DateFormat df = new SimpleDateFormat("HH:mm:ss");

		final Font font = new Font(Font.SANS_SERIF, Font.BOLD, CLOCK_FONTSIZE);

		// デフォルトの時計の色。薄いグレー。
		final Color defaultColor = new Color(240, 240, 240);

		// 時計の点滅
		boolean blinkFlag = true;

		@Override
		public void run() {
			// 点滅のタイマーをセット
			javax.swing.Timer blinkTimer = new javax.swing.Timer(200,
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							blinkFlag = !blinkFlag;
							repaint();
						}
					});
			blinkTimer.start();

			// 1秒毎に時計の時間を更新する。
			while (true) {
				time = df.format(Calendar.getInstance().getTime());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 時計を描画する。
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(font);

			// パネルの真ん中の位置を取得
			FontMetrics fm = g.getFontMetrics();
			int x = (getWidth() - fm.stringWidth(time)) / 2;
			int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

			if (totalScore.getMyScore() < TotalScore.MAX_SCORE) {
				g.setColor(defaultColor);
				g.drawString(time, x, y);

				// scoreに応じて、時計を出現させる
				g.setColor(Color.WHITE);
				double percentScore = totalScore.getMyScore()
						/ (double) TotalScore.MAX_SCORE;
				int closeHeight = (int) (CLOCK_HEIGHT - (CLOCK_HEIGHT * percentScore));
				g.fillRect(0, 0, CLOCK_WIDTH, closeHeight);
			} else {
				g.setColor(Color.RED);
				if (blinkFlag) {
					g.drawString(time, x, y);
				}
			}

		}
	}

}
