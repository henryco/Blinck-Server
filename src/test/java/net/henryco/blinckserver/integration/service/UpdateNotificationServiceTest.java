package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author Henry on 06/09/17.
 */
public class UpdateNotificationServiceTest extends BlinckUserIntegrationTest {


	private @Autowired
	UpdateNotificationService notificationService;



	private static UpdateNotificationService.SimpleNotification
	createRandomNotification(Long userId) throws Exception {

		UpdateNotificationService.SimpleNotification notification =
				new UpdateNotificationService.SimpleNotification();

		notification.setTargetUserId(userId);
		notification.setNotification(TestUtils.randomGaussNumberString());
		notification.setType(TestUtils.randomGaussNumberString());

		Thread.sleep(2);
		return notification;
	}



	@Test @Transactional
	public void addNotificationTest() {

		Long someId = Long.decode(TestUtils.randomGaussNumberString());
		Long notificationId = notificationService.addNotification(someId, "WOW", "HEY");

		UpdateNotification byId = notificationService.getById(notificationId);
		assert byId.getTargetUserId().equals(someId);
		assert byId.getDetails().getType().equals("WOW");
		assert byId.getDetails().getNotification().equals("HEY");

		List<UpdateNotification> allUsers = notificationService.getAllUserNotifications(someId);
		assert allUsers.size() == 1;
		assert allUsers.get(0).equals(byId);
	}


	@Test @Transactional
	public void addSimpleNotificationTest() {

		Long someId = Long.decode(TestUtils.randomGaussNumberString());

		UpdateNotificationService.SimpleNotification notification =
				new UpdateNotificationService.SimpleNotification();
		notification.setTargetUserId(someId);
		notification.setType("someType");
		notification.setNotification("someNotification");

		Long notificationId = notificationService.addNotification(notification);

		UpdateNotification byId = notificationService.getById(notificationId);
		assert byId.getTargetUserId().equals(someId);
		assert byId.getDetails().getType().equals("someType");
		assert byId.getDetails().getNotification().equals("someNotification");

		List<UpdateNotification> allUsers = notificationService.getAllUserNotifications(someId);
		assert allUsers.size() == 1;
		assert allUsers.get(0).equals(byId);
	}


	@Test @Transactional
	public void countGetAndDeleteAllUserNotificationsTest() throws Exception {

		Long someId = Long.decode(TestUtils.randomGaussNumberString());
		int numb = Math.max(new Random().nextInt(10), 2);

		for (int i = 0; i < numb; i++)
			notificationService.addNotification(createRandomNotification(someId));
		assert notificationService.countAllUserNotifications(someId) == numb;

		List<UpdateNotification> all = notificationService.getAllUserNotifications(someId);
		assert all.size() == numb;

		for (int i = 1; i < numb; i++) {
			assert all.get(i).getDate().before(all.get(i - 1).getDate());
		}

		notificationService.removeAllUserNotifications(someId);
		assert notificationService.countAllUserNotifications(someId) == 0;
		assert notificationService.getAllUserNotifications(someId).size() == 0;
	}


	@Test @Transactional
	public void popAllNotificationsTest() throws Exception {

		Long someId = Long.decode(TestUtils.randomGaussNumberString());
		int numb = Math.max(new Random().nextInt(20), 10);

		for (int i = 0; i < numb; i++)
			notificationService.addNotification(createRandomNotification(someId));
		assert notificationService.countAllUserNotifications(someId) == numb;
		assert notificationService.getAllUserNotifications(someId).size() == numb;

		List<UpdateNotification> all = notificationService.popAllNotifications(someId);
		assert all.size() == numb;
		assert notificationService.countAllUserNotifications(someId) == 0;

		assert notificationService.popAllNotifications(someId).size() == 0; // check again by the way
	}

}