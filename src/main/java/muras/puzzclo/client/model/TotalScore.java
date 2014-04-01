/*
 * copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.model;

import java.util.ArrayList;
import java.util.List;

import muras.puzzclo.client.event.ScoreChangeEvent;
import muras.puzzclo.client.event.ScoreListener;
import static muras.puzzclo.client.utils.PuzzcloMessages.*;

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

	private final List<ScoreListener> listeners = new ArrayList<ScoreListener>();

	private boolean isTwoPlay = false;

	// 先攻か後攻か
	private boolean isFirst = true;

	public int getMyScore() {
		return myScore;
	}

	public int getLastScore() {
		return lastOneScore;
	}

	/**
	 * 得点を加算して、オブザーバにnotify。一人プレイ・二人プレイともに使用。
	 * 
	 * @param score
	 *            得点
	 */
	public synchronized void addLastScore(int score) {
		lastOneScore = score;
		myScore += score;

		if (myScore < 0) {
			myScore = 0;
		} else if (myScore > MAX_SCORE) {
			myScore = MAX_SCORE;
		}

		String scoreMessage = isTwoPlay ? getMyScoreMessage(lastOneScore,
				myScore) : getScoreMessage(lastOneScore, myScore);
		notifyToListeners(scoreMessage);
	}

	/**
	 * 得点を減算して、オブザーバにnotify。二人プレイ時に使用。
	 * 
	 * @param score
	 *            得点
	 */
	public synchronized void subLastScore(int score) {
		lastOneScore = score;
		myScore -= score;

		if (myScore < 0) {
			myScore = 0;
		} else if (myScore > MAX_SCORE) {
			myScore = MAX_SCORE;
		}

		notifyToListeners(getOpponentScoreMessage(lastOneScore, myScore));
	}

	/**
	 * notifyはせずに、初期化
	 */
	public synchronized void initScore() {
		lastOneScore = 0;
		myScore = 0;
		isTwoPlay = false;
		this.isFirst = true;
	}

	/**
	 * 二人プレイの時の初期化
	 */
	public synchronized void initScoreTwoPlay(boolean isFirst) {
		lastOneScore = 0;
		myScore = 50;
		isTwoPlay = true;
		this.isFirst = isFirst;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void addScoreListener(ScoreListener listener) {
		listeners.add(listener);
	}

	public void removeScoreListener(ScoreListener listener) {
		listeners.remove(listener);
	}

	private void notifyToListeners(String scoreMessage) {
		for (ScoreListener listener : listeners) {
			listener.scoreChanged(new ScoreChangeEvent(this, scoreMessage));
		}
	}
}
