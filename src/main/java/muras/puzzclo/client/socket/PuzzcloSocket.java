/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.socket;

import static muras.puzzclo.client.utils.PuzzcloMessages.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import muras.puzzclo.client.model.PuzzcloState;
import muras.puzzclo.client.model.PuzzcloState.GameState;
import muras.puzzclo.client.model.SocketMatchData;
import muras.puzzclo.client.model.TotalScore;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * WebSocket用のソケットクラス
 * 
 * @author muramatsu
 * 
 */
@WebSocket(maxMessageSize = 64 * 1024)
public class PuzzcloSocket {

	private final CountDownLatch closeLatch;

	private Session session;

	// ゲームの得点
	private final TotalScore totalScore;

	// ゲームの状態
	private final PuzzcloState puzzcloState;

	public PuzzcloSocket(TotalScore totalScore, PuzzcloState puzzcloState) {
		this.totalScore = totalScore;
		this.puzzcloState = puzzcloState;

		closeLatch = new CountDownLatch(1);
	}

	public void awaitClose(int duration, TimeUnit unit)
			throws InterruptedException {
		closeLatch.await(duration, unit);

		try {
			if (session != null) {
				session.close(StatusCode.NORMAL, "I'm done");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
		session = null;
		closeLatch.countDown();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		this.session = session;

		SocketMatchData data = new SocketMatchData();
		data.setMyName(puzzcloState.getMyName());
		data.setState(puzzcloState.getGameState().toString());

		String sendMessage = toJson(data);

		try {
			Future<Void> fut;
			fut = session.getRemote().sendStringByFuture(sendMessage);
			fut.get(10, TimeUnit.SECONDS);

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("Got msg: %s%n", msg);

		SocketMatchData matchData = toMatchData(msg);

		if (!matchData.isConnected()) {
			puzzcloState.setGameStateWithMessage(GameState.INIT,
					DISCONNECTED_MESSAGE);
		}

		switch (puzzcloState.getGameState()) {
		case CONNECT_SERVER_AS_SERVER:
			puzzcloState.setGameState(GameState.WAIT_CONNECT,
					matchData.getMyName());

			break;

		case CONNECT_SERVER_AS_CLIENT:
			puzzcloState.setGameState(GameState.SELECT_OPPONENT,
					matchData.getMyName(), matchData.getOpponentList());

			break;

		case SELECT_OPPONENT:
			if (matchData.getOpponentName().equals("")) {
				// 対戦相手がいない、または対戦中
				puzzcloState.setGameStateWithMessage(GameState.SELECT_OPPONENT,
						NOT_FOUND_SEND_OPPONENT_MESSAGE);
			} else {
				// 対戦相手がいる
				puzzcloState.setGameStateWithMessage(
						GameState.START_PLAY_TWO_PERSON,
						getVersusMessage(matchData.getMyName(),
								matchData.getOpponentName()));

				if (matchData.isMyTurn()) {
					totalScore.initScoreTwoPlay(true);
					puzzcloState.setGameState(GameState.MY_TURN);
				} else {
					totalScore.initScoreTwoPlay(false);
					puzzcloState.setGameState(GameState.OPPONENT_TURN);
				}
			}

			break;

		case WAIT_CONNECT:
			puzzcloState.setGameStateWithMessage(
					GameState.START_PLAY_TWO_PERSON,
					getVersusMessage(matchData.getMyName(),
							matchData.getOpponentName()));

			if (matchData.isMyTurn()) {
				totalScore.initScoreTwoPlay(true);
				puzzcloState.setGameState(GameState.MY_TURN);
			} else {
				totalScore.initScoreTwoPlay(false);
				puzzcloState.setGameState(GameState.OPPONENT_TURN);
			}
			break;

		case MY_TURN:
		case OPPONENT_TURN:
			// 相手から得点を受け取るとき

			if (matchData.getState().equals(GameState.GAME_LOST.toString())) {
				// 負けが決まった時
				totalScore.subLastScore(matchData.getLastOneScore());
				puzzcloState.setGameState(GameState.GAME_LOST);
			} else {
				// クリア以外
				totalScore.subLastScore(matchData.getLastOneScore());
				puzzcloState.setGameState(GameState.MY_TURN);
			}

			break;

		default:
			// 何もしない
			break;
		}
	}

	/**
	 * カウントダウンすることで、awaitClose()を進める
	 */
	void closeSession() {
		closeLatch.countDown();
	}

	/**
	 * サーバに対戦相手の名前を送信する。
	 * 
	 * @param opponentName
	 */
	void sendOpponentName(String opponentName) {
		SocketMatchData data = new SocketMatchData();
		data.setMyName(puzzcloState.getMyName());
		data.setOpponentName(opponentName);
		data.setState(puzzcloState.getGameState().toString());

		String sendMessage = toJson(data);

		try {
			Future<Void> fut;
			fut = session.getRemote().sendStringByFuture(sendMessage);
			fut.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * サーバにスコアを送信する。
	 */
	void sendScore() {
		SocketMatchData data = new SocketMatchData();
		data.setLastOneScore(totalScore.getLastScore());
		data.setState(puzzcloState.getGameState().toString());
		String sendMessage = toJson(data);

		try {
			Future<Void> fut = null;
			fut = session.getRemote().sendStringByFuture(sendMessage);
			fut.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ゲームに勝った時のサーバに対するメッセージ送信
	 */
	void sendGameWin() {
		// sendScoreとやることは同じ。ただし、stateは違う。
		sendScore();
	}

	private String toJson(SocketMatchData data) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = "";
		try {
			jsonData = mapper.writeValueAsString(data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonData;
	}

	private SocketMatchData toMatchData(String json) {
		ObjectMapper mapper = new ObjectMapper();
		SocketMatchData data = null;

		try {
			data = mapper.readValue(json, SocketMatchData.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
