/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.view;

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

import static muras.puzzclo.utils.ComponentSize.*;

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
	// 時計部分のパネル
	private final Clock clock = createClock();

	/**
	 * コンストラクタ
	 */
	ClockPanel() {
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
	private static class Clock extends JPanel implements Runnable {
		private static final long serialVersionUID = 1L;

		String time;
		final DateFormat df = new SimpleDateFormat("HH:mm:ss");

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
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, CLOCK_FONTSIZE));

			// パネルの真ん中に時計を表示
			final FontMetrics fm = g.getFontMetrics();
			final int xPos = (getWidth() - fm.stringWidth(time)) / 2;
			final int yPos = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
			g.drawString(time, xPos, yPos);
		}
	}

}
