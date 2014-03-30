/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.socket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import muras.puzzclo.client.model.PuzzcloState;
import muras.puzzclo.client.model.SocketMatchData;
import muras.puzzclo.client.model.TotalScore;
import muras.puzzclo.client.model.PuzzcloState.GameState;

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
			puzzcloState.setGameState(GameState.INIT);
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

		default:
			// 何もしない
			break;
		}
	}

	Session getSession() {
		return session;
	}

	/**
	 * カウントダウンすることで、awaitClose()を進める
	 */
	void closeSession() {
		closeLatch.countDown();
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
