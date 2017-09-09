package net.henryco.blinckserver.integration.controller.stomp;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * @author Henry on 09/09/17.
 */

public class SimpleStompControllerTest extends BlinckUserIntegrationTest {

	private static final String USERNAME_HEADER = "User";
	private static final String TOKEN_HEADER = "Authorization";

	private static final String ENDPOINT = "/stomp/chat";
	private static final String APP = "/app";
	private static final String TOPIC = "/topic";
	private static final String QUEUE = "/queue";


	private String endpoint() {
		return "ws://localhost:" + port + ENDPOINT;
	}

	private BlockingQueue<String> blockingQueue;
	private WebSocketStompClient stompClient;


	@Before
	public void setup() {
		blockingQueue = new LinkedBlockingDeque<>();
		stompClient = new WebSocketStompClient(new SockJsClient(
				Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))
		));
	}


	@Test
	public void justReceiveMessageTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 1);
		String token = getForUserAuthToken(users[0]);

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add(USERNAME_HEADER, users[0].toString());
		stompHeaders.add(TOKEN_HEADER, token);

		StompSessionHandler handler = new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				super.afterConnected(session, connectedHeaders);
			}
		};

		StompSession session = stompClient.connect(
				new URI(endpoint()),
				headers,
				stompHeaders,
				handler
		).get();

		session.subscribe(TOPIC, new DefaultStompFrameHandler());
		session.send(APP + "/test", "SOME RANDOM TEXT".getBytes());


	}



	class DefaultStompFrameHandler implements StompFrameHandler {

		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return String.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			blockingQueue.offer(new String((byte[]) o));
			System.out.println("WORK");
		}

	}
}