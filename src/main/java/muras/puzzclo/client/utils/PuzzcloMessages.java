/*
 * copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.utils;

import java.util.List;

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

	public static final String DISCONNECTED_MESSAGE = "切断されました。\n\n";

	public static final String NO_OPPONENT_MESSAGE = "対戦相手はいませんでした。\nqを入力して初期画面に戻ってください。\n\n";

	public static final String NOT_FOUND_SEND_OPPONENT_MESSAGE = "指定した対戦相手は存在しない、もしくは対戦中でした。\n\n";

	public static final String WIN_MESSAGE = "あなたの勝ちです。\n\nqを入力すると初期画面に戻ります。\n\n";

	public static final String LOST_MESSAGE = "あなたの負けです。\n\nqを入力すると初期画面に戻ります。\n\n";

	public static String getScoreMessage(int lastOneScore, int totalScore) {
		return "あなたは" + lastOneScore + "点獲得しました。\nあなたの現在のスコア: " + totalScore
				+ "\n\n";
	}

	public static String getMyScoreMessage(int lastOneScore, int totalScore) {
		return "あなたは" + lastOneScore + "点獲得しました。\nあなたの現在のスコア: " + totalScore
				+ "\n対戦相手の現在のスコア: " + (100 - totalScore) + "\n\n";
	}

	public static String getOpponentScoreMessage(int lastOneScore,
			int totalScore) {
		return "対戦相手は" + lastOneScore + "点獲得しました。\nあなたの現在のスコア: " + totalScore
				+ "\n対戦相手の現在のスコア: " + (100 - totalScore) + "\n\n";
	}

	public static String getWelcomeMessage(String name) {
		return "ようこそ " + name + "さん\n\n";
	}

	public static String getVersusMessage(String myName, String opponentName) {
		return myName + " vs " + opponentName + "\n";
	}

	public static String getSelectOpponentMessage(List<String> opponentList) {

		StringBuilder msg = new StringBuilder(
				"--------------------\n接続待ちの相手一覧\n--------------------\n");

		for (String opponentName : opponentList) {
			msg.append(opponentName + "\n");
		}

		msg.append("--------------------\n\n");

		return msg.toString();
	}

}
