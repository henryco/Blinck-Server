package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;

import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Henry on 06/09/17.
 */
public class UserNotificationControllerTest extends BlinckUserIntegrationTest {


	private @Autowired
	UpdateNotificationService notificationService;


	private static final String NOTIFICATIONS = "/protected/user/notifications";
	private static final String COUNT = NOTIFICATIONS + "/count";
	private static final String LAST = NOTIFICATIONS + "/last";
	private static final String LIST = NOTIFICATIONS + "/list";
	private static final String ALL = LIST + "/all";
	private static final String POP = ALL + "/pop";

	private static final String REMOVE = NOTIFICATIONS + "/remove?id=";
	private static final String REMOVE_ALL = NOTIFICATIONS + "/remove/all";


	private @SuppressWarnings("unused")
	static final class TestNotification
			implements Serializable {

		public Long id;
		public String type;
		public String info;
		public Date timestamp;
	}



	@Test
	public void notificationsCountTest() throws Exception {

		final Long[] users = saveNewRandomUsers(this, 2);

		final int numb1 = randomInt(5, 10);
		final int numb2 = randomInt(10, 15);

		for (int i = 0; i < numb1; i++) saveRandomNotification(this, users[0]);
		for (int i = 0; i < numb2; i++) saveRandomNotification(this, users[1]);

		Long count1 = authorizedGetRequest(COUNT, getForUserAuthToken(users[0]), Long.class).getBody();
		Long count2 = authorizedGetRequest(COUNT, getForUserAuthToken(users[1]), Long.class).getBody();

		assert count1.equals(((long) numb1));
		assert count2.equals(((long) numb2));
	}



	@Test
	public void lastNotificationTest() throws Exception {

		final int n = randomInt(5, 10);
		final String[] msg = new String[n];
		final String[] typ = new String[n];
		final Long[] users = saveNewRandomUsers(this, n);

		final Date date0 = new Date(System.currentTimeMillis());

		loop2n(n, (i, k) -> {

			msg[i] = TestUtils.randomGaussNumberString();
			typ[i] = TestUtils.randomGaussNumberString();
			saveRandomNotification(this, users[i], msg[i], typ[i]);
		});

		final Date date1 = new Date(System.currentTimeMillis());

		for (int i = 0; i < n; i++) {

			final String token = getForUserAuthToken(users[i]);
			final TestNotification last = authorizedGetRequest(LAST, token, TestNotification.class).getBody();

			assert last.id != null;
			assert last.info.equals(msg[i]);
			assert last.type.equals(typ[i]);
			assert last.timestamp.after(date0) && last.timestamp.before(date1);
		}
	}



	@Test
	public void listAllNotificationsTest() throws Exception {

		final int n = randomInt(5, 10);
		final Long[] users = saveNewRandomUsers(this, n);
		int[] msgs = loop2n(n, (i, k) -> saveRandomNotification(this, users[i]));

		for (int i = 0; i < n; i++) {

			final String token = getForUserAuthToken(users[i]);
			final TestNotification[] all = authorizedGetRequest(ALL, token, TestNotification[].class).getBody();

			assert all.length != 0;
			assert all.length == msgs[i];

			for (int z = 1; z < all.length; z++) {
				assert all[z].timestamp.before(all[z - 1].timestamp);
			}
		}
	}



	@Test
	public void listNotificationsTest() throws Exception {

		final Long[] users = saveNewRandomUsers(this, randomInt(5, 10));
		loop2n(users.length, (i, k) -> saveRandomNotification(this, users[i]));

		for (Long user : users) {

			final String token = getForUserAuthToken(user);

			final Long count = authorizedGetRequest(COUNT, token, Long.class).getBody();
			final TestNotification[] all = authorizedGetRequest(ALL, token, TestNotification[].class).getBody();

			final int size = randomInt(3, 9);
			final String request = LIST + "?page=0&size=" + size;
			final TestNotification[] list = authorizedGetRequest(request, token, TestNotification[].class).getBody();

			assert all.length == count;
			assert list.length <= count;
			assert list.length == size;
		}
	}



	@Test
	public void popAllNotificationsTest() throws Exception {

		final Long[] users = saveNewRandomUsers(this, randomInt(5, 10));
		loop2n(users.length, (i, k) -> saveRandomNotification(this, users[i]));

		for (Long user: users) {

			final String token = getForUserAuthToken(user);

			final Long countBefore = authorizedGetRequest(COUNT, token, Long.class).getBody();
			assert countBefore != 0;

			final TestNotification[] list = authorizedGetRequest(POP, token, TestNotification[].class).getBody();
			assert list.length == countBefore;

			final Long countAfter = authorizedGetRequest(COUNT, token, Long.class).getBody();
			assert countAfter == 0;
		}
	}



	@Test
	public void removeAllNotificationsTest() throws Exception {

		final Long[] users = saveNewRandomUsers(this, randomInt(5, 10));
		loop2n(users.length, (i, k) -> saveRandomNotification(this, users[i]));

		for (Long user : users) authorizedDeleteRequest(REMOVE_ALL, getForUserAuthToken(user));

		for (Long user : users) {
			final Long countAfter = authorizedGetRequest(COUNT, getForUserAuthToken(user), Long.class).getBody();
			assert countAfter.equals(0L);
		}
	}



	@Test
	public void directRemoveNotificationTest() throws Exception {

		final Long[] users = saveNewRandomUsers(this, randomInt(5, 10));
		int[] size = loop2n(users.length, (i, k) -> saveRandomNotification(this, users[i]));

		for (int i = 0; i < users.length; i++) {

			final String token = getForUserAuthToken(users[i]);
			final Long target = notificationService.getAllUserNotifications(users[i]).get(randomInt(0, size[i])).getId();

			final Long countBefore = authorizedGetRequest(COUNT, token, Long.class).getBody();

			authorizedDeleteRequest(REMOVE + target, token);

			final Long countAfter = authorizedGetRequest(COUNT, token, Long.class).getBody();

			assert countBefore != 0;
			assert countAfter.equals(countBefore - 1);
			assert !notificationService.isExists(target);
		}
	}





	private static int[] loop2n(int n, BiConsumer<Integer, Integer> action) {
		final int[] ints = new int[n];
		for (int i = 0; i < n; i++) {
			ints[i] = randomInt(10, 20);
			for (int k = 0; k < ints[i]; k++) {
				action.accept(i, k);
			}
		}
		return ints;
	}


	private static int
	randomInt(int min, int max) {
		return Math.max(min, new Random().nextInt(max));
	}


	private static void
	saveRandomNotification(UserNotificationControllerTest context,
						   Long userId)  {
		saveRandomNotification(
				context, userId,
				TestUtils.randomGaussNumberString()
		);
	}

	private static void
	saveRandomNotification(UserNotificationControllerTest context,
						   Long userId,
						   String msg)  {
		saveRandomNotification(
				context, userId, msg, TestUtils.randomGaussNumberString()
		);
	}

	private static void
	saveRandomNotification(UserNotificationControllerTest context,
						   Long userId,
						   String msg,
						   String type)  {
		try {
			context.notificationService.addNotification(
					userId,
					type,
					msg
			);
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}