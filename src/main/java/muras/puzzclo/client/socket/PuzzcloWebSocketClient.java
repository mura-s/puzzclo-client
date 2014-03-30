/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.socket;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import muras.puzzclo.client.event.GameStateChangeEvent;
import muras.puzzclo.client.event.GameStateListener;
import muras.puzzclo.client.model.PuzzcloState;
import muras.puzzclo.client.model.TotalScore;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 * WebSocket通信を行うクライアント
 * 
 * @author muramatsu
 * 
 */
public class PuzzcloWebSocketClient implements GameStateListener {
	private final String dstUri = "ws://localhost:8080/websocket/";

	// ゲームの得点
	private final TotalScore totalScore;

	// ゲームの状態
	private final PuzzcloState puzzcloState;

	private final PuzzcloSocket socket;

	private final WebSocketClient client;

	public PuzzcloWebSocketClient(TotalScore totalScore,
			PuzzcloState puzzcloState) {
		this.totalScore = totalScore;

		this.puzzcloState = puzzcloState;
		puzzcloState.addGameStateListener(this);

		this.socket = new PuzzcloSocket(totalScore, puzzcloState);

		this.client = new WebSocketClient();
	}

	/**
	 * WebSocketサーバーに接続する。
	 */
	private void connect() {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.start();
					URI uri = new URI(dstUri);
					ClientUpgradeRequest request = new ClientUpgradeRequest();

					client.connect(socket, uri, request);

					// サーバとの接続は最大2時間
					socket.awaitClose(2, TimeUnit.HOURS);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						client.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		th.start();
	}

	/**
	 * 切断する。 <br />
	 * 
	 * closeSession()をするとawaitしていたスレッドが起きるので、client.stop()される。
	 */
	private void disconnect() {
		try {
			socket.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameStateChanged(GameStateChangeEvent e) {
		switch (e.getSource()) {
		case INIT:
			disconnect();

			break;

		case CONNECT_SERVER_AS_SERVER:
		case CONNECT_SERVER_AS_CLIENT:
			connect();

			break;

		default:
			// 何もしない
			break;
		}
	}

}
