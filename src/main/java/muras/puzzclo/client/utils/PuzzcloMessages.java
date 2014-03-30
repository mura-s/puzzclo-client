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

	public static final String TITLE_MESSAGE = "==========\n     パズクロ！\n==========\n\n";

	public static final String INIT_MESSAGE = "数字をどれか入力してください。\n" + "1. 1人プレイ"
			+ "\n2. 2人プレイ(相手を待つ)\n3. 2人プレイ(待っている人の中から相手を選ぶ)\n\n";

	public static final String INPUT_YOUR_NAME_MESSAGE = "あなたの名前を入力してください。\n\n";

	public static final String INPUT_OPPONENT_MESSAGE = "対戦相手の名前を入力してください。\n\n";

	public static final String WAIT_CONNECT_MESSAGE = "対戦相手を待っています…\n\n";

	public static final String CONNECT_SERVER_MESSAGE = "サーバに接続中…\n\n";

	public static final String GAME_START_MESSAGE = "ゲームスタート！\n\n";

	public static final String GAME_CLEAR_MESSAGE = "ゲームクリア！\n\nqを入力すると初期画面に戻ります。\n\n";
	
	public static final String MY_TURN_MESSAGE = "あなたのターンです。\n\n";
	
	public static final String OPPONENT_TURN_MESSAGE = "相手のターンです。\n\n";
	
	public static String getScoreMessage(int lastOneScore, int totalScore) {
		return lastOneScore + "点獲得しました。\n現在のスコア: " + totalScore + "\n\n";
	}

	public static String getWelcomeMessage(String name) {
		return "ようこそ " + name + "さん\n\n";

	}

}
