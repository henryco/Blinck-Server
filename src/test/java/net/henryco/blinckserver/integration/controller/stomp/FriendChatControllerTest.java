package net.henryco.blinckserver.integration.controller.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.henryco.blinckserver.integration.BlinckStompIntegrationTest;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Henry on 11/09/17.
 */ @SuppressWarnings("unused")
public class FriendChatControllerTest extends BlinckStompIntegrationTest {

	private static final String REST_POST = "/protected/user/friends/conversation/messages/send";
	private static final String REST_GET = "/protected/user/friends/conversation/messages/list?page=0&size=1000&id=";

	private static final String STOMP_NOTIF = "/user/queue/notification";
	private static final String STOMP_POST = "/app/message/friendship";
	private static final String STOMP_GET = "/user/message/friendship/";
	private static final String STOMP_STAT = STOMP_GET + "stat";

	private static final String TYPE_REST = "friendship_message_rest";
	private static final String TYPE_STOMP = "friendship_message_stomp";


	private static final class TestPostForm
			implements Serializable {
		public Long friendship;
		public String message;
		public Date timestamp;
	}

	private static final class TestGetForm
			implements Serializable{
		public Long id;
		public Long friendship;
		public Long author;
		public String message;
		public Date timestamp;
	}

	private static final class TestNotificationForm
			implements Serializable {
		public Long id;
		public String type;
		public String info;
		public Date timestamp;
	}

	private static final class TestStatusForm
			implements Serializable {
		public String destination;
		public Date timestamp;
		public boolean status;
	}


	private @Autowired FriendshipService friendshipService;

	private Long[] users;
	private Long relation;
	private String[] tokens;

	@Before
	public void beforeRelation() throws Exception {
		users = saveNewRandomUsers(this, 2);
		relation = friendshipService.addFriendshipRelation(users[0], users[1]);
		tokens = new String[]{
				getForUserAuthToken(users[0]),
				getForUserAuthToken(users[1])
		};
	}



	@Test
	public void sendByRestGetByStompTest() throws Exception {

		final StompSessionHandler handler = new StompSessionHandlerAdapter() {};
		final DefaultStompFrameHandler resultHandler = new DefaultStompFrameHandler();

		StompSession session = createSession(users[0], tokens[0], handler, 2);
		session.subscribe(STOMP_NOTIF, resultHandler);

		final AtomicBoolean flag = new AtomicBoolean(false);
		final String[] result = new String[1];

		new Thread((TestRunnable) () -> {
			result[0] = resultHandler.getBlocked().poll(2, SECONDS);
			flag.set(true);
		}).start();

		TestPostForm postForm = new TestPostForm();
		postForm.friendship = relation;
		postForm.message = TestUtils.randomGaussNumberString();

		authorizedPostRequest(REST_POST, tokens[1], postForm);

		while (!flag.get()) Thread.sleep(100);

		TestNotificationForm notification = new ObjectMapper().readValue(result[0], TestNotificationForm.class);
		assert notification.type.equals(TYPE_REST);
		assert notification.info.equals(relation.toString());

		TestPostForm[] body = authorizedGetRequest(REST_GET + relation, tokens[0], TestPostForm[].class).getBody();
		assert body[0].friendship.equals(relation);
		assert body[0].message.equals(postForm.message);
	}



//	@Test
	public void sendAndGetByStomp() throws Exception {

		final StompSessionHandler handler1 = new StompSessionHandlerAdapter() {};
		final ListStompFrameHandler notifHandler1 = new ListStompFrameHandler();
		final ListStompFrameHandler statHandler1 = new ListStompFrameHandler();
		final ListStompFrameHandler getHandler1 = new ListStompFrameHandler();

		final StompSessionHandler handler2 = new StompSessionHandlerAdapter() {};
		final ListStompFrameHandler notifHandler2 = new ListStompFrameHandler();
		final ListStompFrameHandler statHandler2 = new ListStompFrameHandler();
		final ListStompFrameHandler getHandler2 = new ListStompFrameHandler();


		StompSession session1 = createSession(users[0], tokens[0], handler1, 3);
		session1.subscribe(STOMP_NOTIF, notifHandler1);
		session1.subscribe(STOMP_STAT, statHandler1);
		session1.subscribe(STOMP_GET + relation, getHandler1);

		StompSession session2 = createSession(users[1], tokens[1], handler2, 3);
		session2.subscribe(STOMP_NOTIF, notifHandler2);
		session2.subscribe(STOMP_STAT, statHandler2);
		session2.subscribe(STOMP_GET + relation, getHandler2);

		final String[] messages1 = {
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString()
		};

		final String[] messages2 = {
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString(),
		};


		session1.send(STOMP_POST, new ObjectMapper().writeValueAsString(createPost(messages1[0], relation)).getBytes());
		Thread.sleep(50);
		session2.send(STOMP_POST, new ObjectMapper().writeValueAsString(createPost(messages2[0], relation)).getBytes());
		Thread.sleep(50);
		session2.send(STOMP_POST, new ObjectMapper().writeValueAsString(createPost(messages2[1], relation)).getBytes());
		Thread.sleep(50);
		session1.send(STOMP_POST, new ObjectMapper().writeValueAsString(createPost(messages1[1], relation)).getBytes());
		Thread.sleep(50);
		session1.send(STOMP_POST, new ObjectMapper().writeValueAsString(createPost(messages1[2], relation)).getBytes());


		while (getHandler1.get().size() != messages2.length || getHandler2.get().size() != messages1.length)
			Thread.sleep(100);

		while (statHandler1.get().size() != messages1.length || statHandler2.get().size() != messages2.length)
			Thread.sleep(100);

		while (notifHandler1.get().size() != messages2.length || notifHandler2.get().size() != messages1.length)
			Thread.sleep(100);


		for (int i = 0; i < messages1.length; i++) {

			TestGetForm form = new ObjectMapper().readValue(getHandler2.get().get(i), TestGetForm.class);
			assert form.message.equals(messages1[i]);
			assert form.friendship.equals(relation);
			assert form.author.equals(users[0]);

			TestStatusForm stat = new ObjectMapper().readValue(statHandler1.get().get(i), TestStatusForm.class);
			assert stat.status;
			assert stat.destination.equals(relation.toString());

			TestNotificationForm notif = new ObjectMapper().readValue(notifHandler2.get().get(i), TestNotificationForm.class);
			assert notif.type.equals(TYPE_STOMP);
			assert notif.info.equals(relation.toString());
		}

		for (int i = 0; i < messages2.length; i++) {

			TestGetForm form = new ObjectMapper().readValue(getHandler1.get().get(i), TestGetForm.class);
			assert form.message.equals(messages2[i]);
			assert form.friendship.equals(relation);
			assert form.author.equals(users[1]);

			TestStatusForm stat = new ObjectMapper().readValue(statHandler2.get().get(i), TestStatusForm.class);
			assert stat.status;
			assert stat.destination.equals(relation.toString());

			TestNotificationForm notif = new ObjectMapper().readValue(notifHandler1.get().get(i), TestNotificationForm.class);
			assert notif.type.equals(TYPE_STOMP);
			assert notif.info.equals(relation.toString());
		}
	}


	private static TestPostForm createPost(String msg, Long relation) {
		TestPostForm postForm = new TestPostForm();
		postForm.message = msg;
		postForm.friendship = relation;
		postForm.timestamp = new Date(System.currentTimeMillis());
		return postForm;
	}



	public static final class
	ListStompFrameHandler
			implements StompFrameHandler {

		private final List<String> list
				= new LinkedList<>();

		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			String income = new String(((byte[]) o));
			list.add(income);
		}

		public List<String> get() {
			return list;
		}
	}
}