package net.henryco.blinckserver.integration.controller.stomp;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * @author Henry on 09/09/17.
 */

public class SimpleStompControllerTest extends BlinckUserIntegrationTest {

	private static final String USERNAME_HEADER = "User";
	private static final String TOKEN_HEADER = HttpHeaders.AUTHORIZATION;

	private static final String ENDPOINT = "/stomp";
	private static final String APP = "/app";
	private static final String TOPIC = "/topic";
	private static final String QUEUE = "/queue";


	private String endpoint() {
		return "ws://localhost:" + port + ENDPOINT;
	}


	private WebSocketStompClient stompClient;


	@Before
	public void setup() {
		stompClient = new WebSocketStompClient(new SockJsClient(
				Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))
		));
	}


	@Test
	public void justReceiveMessageTest() throws Exception {

		final StompSessionHandler handler = new StompSessionHandlerAdapter() {};

		Long[] users = saveNewRandomUsers(this, 2);
		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[1]);

		DefaultStompFrameHandler resultHandler1 = new DefaultStompFrameHandler();
		DefaultStompFrameHandler resultHandler2 = new DefaultStompFrameHandler();
		DefaultStompFrameHandler resultHandler3 = new DefaultStompFrameHandler();

		StompSession session1 = createSession(users[0], token1, handler, 5);
		StompSession session2 = createSession(users[1], token2, handler, 5);

		session1.subscribe("/user/queue/test", resultHandler1);
		session2.subscribe("/user/queue/test", resultHandler2);
		session2.subscribe("/message/wow", resultHandler3);

		new Thread(() -> {
			session1.send(APP + "/test", "SOME RANDOM TEXT".getBytes());
			session2.send(APP + "/test", "OTHER TEXT".getBytes());
		}).start();


		new Thread(() -> {
			try {
				System.out.println("\nuser22: " + resultHandler3.get().poll(5, SECONDS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();


		new Thread(() -> {
			try {
				System.out.println("user1: "+resultHandler1.get().poll(5, SECONDS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();



		new Thread(() -> {
			try {
				System.out.println("user2: "+resultHandler2.get().poll(5, SECONDS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();




		Thread.sleep(6000);
	}




	private StompSession createSession(Long user,
									   String accessToken,
									   StompSessionHandler handler,
									   int time) throws Exception {
		return stompClient.connect(
				new URI(endpoint()),
				createHttpAuthHeaders(accessToken),
				createAuthHeaders(user, accessToken),
				handler
		).get(time, SECONDS);
	}


	private static final class
	DefaultStompFrameHandler
			implements StompFrameHandler {

		private BlockingQueue<String> blockingQueue
				= new LinkedBlockingDeque<>();

		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			String income = new String(((byte[]) o));
			blockingQueue.offer(income);
		}

		public BlockingQueue<String> get() {
			return blockingQueue;
		}
	}


	private static
	StompHeaders createAuthHeaders(Long user, String token) {
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add(USERNAME_HEADER, user.toString());
		stompHeaders.add(TOKEN_HEADER, token);
		return stompHeaders;
	}

	private static
	WebSocketHttpHeaders createHttpAuthHeaders(String token) {
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add(TOKEN_HEADER, token);
		return headers;
	}

}