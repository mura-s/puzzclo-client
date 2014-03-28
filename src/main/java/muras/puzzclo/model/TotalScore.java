/*
 * copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.model;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.event.ScoreChangeEvent;
import muras.puzzclo.event.ScoreListener;

/**
 * ゲームの現在の得点を表す。
 * 
 * @author muramatsu
 * 
 */
public final class TotalScore {
	public static final int MAX_SCORE = 100;

	private int myScore = 0;

	private int lastOneScore = 0;

	private final List<ScoreListener> listeners = new ArrayList<>();

	public TotalScore() {
		myScore = 0;
	}

	public TotalScore(int score) {
		myScore = score;
	}

	public int getMyScore() {
		return myScore;
	}

	public void addLastScore(int score) {
		lastOneScore = score;
		setMyScore(myScore + score);
	}

	public void subLastScore(int score) {
		lastOneScore = score;
		setMyScore(myScore - score);
	}

	private void setMyScore(int myScore) {
		if (myScore < 0) {
			myScore = 0;
		} else if (myScore > MAX_SCORE) {
			myScore = MAX_SCORE;
		}

		this.myScore = myScore;

		notifyToListeners();
	}

	public void addScoreListener(ScoreListener listener) {
		listeners.add(listener);
	}

	public void notifyToListeners() {
		for (ScoreListener listener : listeners) {
			listener.scoreChanged(new ScoreChangeEvent(this, lastOneScore
					+ "点獲得しました。\n現在のスコア: " + myScore + "\n\n"));

			if (myScore == MAX_SCORE) {
				listener.scoreChanged(new ScoreChangeEvent(this, "ゲームクリア！\n\n"));
			}
		}
	}

}
