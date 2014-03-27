/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

import static muras.puzzclo.utils.ComponentSize.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import muras.puzzclo.model.TotalScore;

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

		/**
		 * 1秒毎に時計の時間を更新する。
		 */
		@Override
		public void run() {
			while (true) {
				time = df.format(Calendar.getInstance().getTime());
				repaint();
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
			
			// 100点以上なら、色を赤くする
			if (totalScore.getMyScore() < TotalScore.MAX_SCORE) {
				g.setColor(Color.LIGHT_GRAY);
			} else {
				g.setColor(Color.RED);
			}

			// パネルの真ん中に時計を表示
			FontMetrics fm = g.getFontMetrics();
			int x = (getWidth() - fm.stringWidth(time)) / 2;
			int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
			g.drawString(time, x, y);

			// scoreに応じて、時計を出現させる
			g.setColor(Color.WHITE);
			double percentScore = totalScore.getMyScore()
					/ (double) TotalScore.MAX_SCORE;
			int closeHeight = (int) (CLOCK_HEIGHT - (CLOCK_HEIGHT * percentScore));
			g.fillRect(0, 0, CLOCK_WIDTH, closeHeight);
		}
	}

}
