/*
 * Copyright (C) 2014 Seiya Muramatsu. All rights reserved.
 */
package muras.puzzclo.client.socket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

	public PuzzcloSocket() {
		closeLatch = new CountDownLatch(1);
	}

	public void awaitClose(int duration, TimeUnit unit)
			throws InterruptedException {
		closeLatch.await(duration, unit);
		
		try {
			if (session != null) {
				session.close(StatusCode.NORMAL, "I'm done");
			} else {
				// TODO: サーバに切られた場合
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
		try {
			Future<Void> fut;
			fut = session.getRemote().sendStringByFuture("Hello");
			fut.get(5, TimeUnit.SECONDS);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("Got msg: %s%n", msg);
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
}
