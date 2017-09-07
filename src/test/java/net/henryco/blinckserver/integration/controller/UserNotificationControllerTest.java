package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;

import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * @author Henry on 06/09/17.
 */
public class UserNotificationControllerTest extends BlinckUserIntegrationTest {

	private static final String NOTIFICATIONS = "/protected/user/notifications";
	private static final String COUNT = NOTIFICATIONS + "/count";


	private @Autowired UpdateNotificationService notificationService;


	@Test
	public void notificationsCountTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 2);

		final int numb1 = randomInt(5, 10);
		final int numb2 = randomInt(10, 15);

		for (int i = 0; i < numb1; i++) saveRandomNotification(this, users[0]);
		for (int i = 0; i < numb2; i++) saveRandomNotification(this, users[1]);

		Long count1 = authorizedGetRequest(COUNT, getForUserAuthToken(users[0]), Long.class).getBody();
		Long count2 = authorizedGetRequest(COUNT, getForUserAuthToken(users[1]), Long.class).getBody();

		assert count1.equals(((long) numb1));
		assert count2.equals(((long) numb2));
	}








	private static void
	saveRandomNotification(UserNotificationControllerTest context,
						   Long userId) throws Exception {

		context.notificationService.addNotification(
				userId,
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString()
		);

		Thread.sleep(1);
	}

	private static int
	randomInt(int min, int max) {
		return Math.max(min, new Random().nextInt(max));
	}


}