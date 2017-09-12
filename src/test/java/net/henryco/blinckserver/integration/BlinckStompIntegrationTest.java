package net.henryco.blinckserver.integration;

import org.junit.Before;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Henry on 11/09/17.
 */
public abstract class BlinckStompIntegrationTest extends BlinckUserIntegrationTest {

	protected static final String USERNAME_HEADER = "User";
	protected static final String TOKEN_HEADER = HttpHeaders.AUTHORIZATION;

	protected static final String ENDPOINT = "/stomp";
	protected static final String APP = "/app";
	protected static final String TOPIC = "/topic";
	protected static final String QUEUE = "/queue";
	protected static final String MESSAGE = "/message";


	protected String endpoint() {
		return "ws://localhost:" + port + ENDPOINT;
	}

	protected WebSocketStompClient stompClient;


	@Before
	public void setup() {
		stompClient = new WebSocketStompClient(new SockJsClient(
				Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))
		));
	}


	protected StompSession createSession(Long user,
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


	protected static StompHeaders createAuthHeaders(Long user, String token) {
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add(USERNAME_HEADER, user.toString());
		stompHeaders.add(TOKEN_HEADER, token);
		return stompHeaders;
	}

	protected static WebSocketHttpHeaders createHttpAuthHeaders(String token) {
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add(TOKEN_HEADER, token);
		return headers;
	}


	public static final class
	DefaultStompFrameHandler
			implements StompFrameHandler {

		private final BlockingQueue<String> blockingQueue
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

		public BlockingQueue<String> getBlocked() {
			return blockingQueue;
		}
	}


	@FunctionalInterface
	public interface TestRunnable extends Runnable {

		default void run() {
			try {
				this.catchRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void catchRun() throws Exception;
	}

}