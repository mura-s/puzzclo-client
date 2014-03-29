/*
 * copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.utils;

/**
 * パズクロで表示するメッセージに関するクラス
 * 
 * @author muramatsu
 * 
 */
public class PuzzcloMessages {
	public static final String INIT_MESSAGE = "どちらかの数字を入力してください。\n"
			+ "1. 1人プレイ" + "\n2. 2人プレイ(未実装)\n\n";

	public static final String GAME_START_MESSAGE = "ゲームスタート！\n\n";

	public static final String GAME_CLEAR_MESSAGE = "ゲームクリア！\n\nqを入力すると初期画面に戻ります。\n\n";

	public static final String TITLE_MESSAGE = "==========\n     パズクロ！\n==========\n\n";

	public static final String getScoreMessage(int lastOneScore, int totalScore) {
		return lastOneScore + "点獲得しました。\n現在のスコア: " + totalScore + "\n\n";
	}
}
