package net.henryco.blinckserver.integration.controller.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.henryco.blinckserver.integration.BlinckStompIntegrationTest;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Henry on 11/09/17.
 */
public class AnyStompNotificationTest extends BlinckStompIntegrationTest {

	private static final String SEND_NOTIFICATION = "/protected/admin/notification/user";
	private static final String NOTIFICATION_DESTINATION = "/user/queue/notification";


	public static final
	class TestNotificationJSON
			implements Serializable {

		public Long id;
		public String type;
		public String info;
		public Date timestamp;

		@Override
		public String toString() {
			return "{" +
					"id=" + id +
					", type='" + type + '\'' +
					", info='" + info + '\'' +
					", timestamp=" + timestamp +
			'}';
		}
	}

	public static final
	class TestNotificationPostJson
			implements Serializable {
		public Long receiver_id;
		public String type;
		public String notification;

		public TestNotificationPostJson(
				Long receiver_id,
				String type,
				String notification) {
			this.receiver_id = receiver_id;
			this.type = type;
			this.notification = notification;
		}
	}


	@Test
	public void anyNotificationTest() throws Exception {

		final AtomicBoolean flag = new AtomicBoolean(false);

		final String randomType = TestUtils.randomGaussNumberString();
		final String randomMsg = TestUtils.randomGaussNumberString();

		Long[] users = saveNewRandomUsers(this, 3);
		String token = getForUserAuthToken(users[0]);

		final StompSessionHandler handler = new StompSessionHandlerAdapter() {};
		final DefaultStompFrameHandler resultHandler = new DefaultStompFrameHandler();

		StompSession session = createSession(users[0], token, handler, 3);
		session.subscribe(NOTIFICATION_DESTINATION, resultHandler);

		final String[] result = new String[1];
		new Thread((TestRunnable) () -> {

			result[0] = resultHandler.getBlocked().poll(3, SECONDS);
			flag.set(true);
		}).start();

		String adminAuthToken = getForAdminAuthToken();
		authorizedPostRequest(
				SEND_NOTIFICATION,
				adminAuthToken,
				new TestNotificationPostJson(users[0], randomType, randomMsg)
		);

		while (!flag.get())
			Thread.sleep(500);

		TestNotificationJSON notification = new ObjectMapper().readValue(result[0], TestNotificationJSON.class);
		assert notification.type.equals(randomType);
		assert notification.info.equals(randomMsg);

	}


}