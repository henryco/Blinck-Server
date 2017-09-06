package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Henry on 06/09/17.
 */
public class UpdateNotificationEntityTest extends BlinckUserIntegrationTest {

	private @Autowired UpdateNotificationDao notificationDao;


	private static void
	assertEquals(UpdateNotification one, UpdateNotification two) {

		assert one.getDate().equals(two.getDate());
		assert one.getTargetUserId().equals(two.getTargetUserId());
		assert one.getDetails().getNotification().equals(two.getDetails().getNotification());
		assert one.getDetails().getType().equals(two.getDetails().getType());
	}

	private static UpdateNotification
	createRandomNotification(Long targetId) {

		UpdateNotification.Details details = new UpdateNotification.Details();
		details.setNotification(TestUtils.randomGaussNumberString());
		details.setType(TestUtils.randomGaussNumberString());

		UpdateNotification notification = new UpdateNotification();
		notification.setTargetUserId(targetId);
		notification.setDate(new Date(System.currentTimeMillis()));
		notification.setDetails(details);

		return notification;
	}



	@Test @Transactional
	public void saveNotificationTest() {

		Long targetId = Long.decode(TestUtils.randomGaussNumberString());

		UpdateNotification notification = createRandomNotification(targetId);
		UpdateNotification saved = notificationDao.save(notification);

		assertEquals(saved, notification);
	}



	@Test @Transactional
	public void deleteNotificationTest() {

		final UpdateNotification[] notifications = new UpdateNotification[9];
		for (int i = 0; i < notifications.length; i++) {

			Long targetId = Long.decode(TestUtils.randomGaussNumberString());
			notifications[i] = createRandomNotification(targetId);
		}

		final UpdateNotification[] saved = new UpdateNotification[notifications.length];
		for (int i = 0; i < saved.length; i++) {
			 saved[i] = notificationDao.save(notifications[i]);
		}

		for (int i = 0; i < saved.length; i++) {
			UpdateNotification by = notificationDao.getById(saved[i].getId());
			assertEquals(by, notifications[i]);
		}

		for (int i = 0; i < saved.length - 1; i += 2) {
			notificationDao.deleteById(saved[i].getId());
		}

		for (int i = 0; i < saved.length - 1; i += 2) {
			assert !notificationDao.isExists(saved[i].getId());
		}

		for (int i = 1; i < saved.length - 2; i += 2) {
			UpdateNotification by = notificationDao.getById(saved[i].getId());
			assertEquals(by, notifications[i]);
		}

	}



	@Test @Transactional
	public void listCountAndDeleteAllNotificationsTest() throws Exception {

		final Long userId1 = Long.decode(TestUtils.randomGaussNumberString());
		final Long userId2 = Long.decode(TestUtils.randomGaussNumberString());

		final UpdateNotification[] saved1 = new UpdateNotification[Math.max(new Random().nextInt(15), 2)];
		final UpdateNotification[] saved2 = new UpdateNotification[Math.max(new Random().nextInt(15), 2)];

		for (int i = 0; i < saved1.length; i++) {
			saved1[i] = notificationDao.save(createRandomNotification(userId1));
			Thread.sleep(2);
		}

		for (int i = 0; i < saved2.length; i++) {
			saved2[i] = notificationDao.save(createRandomNotification(userId2));
			Thread.sleep(2);
		}

		assert notificationDao.countUserNotifications(userId1) == saved1.length;
		assert notificationDao.countUserNotifications(userId2) == saved2.length;

		List<UpdateNotification> all1 = notificationDao.getAllNotificationsByUserIdOrderDesc(userId1);
		List<UpdateNotification> all2 = notificationDao.getAllNotificationsByUserIdOrderDesc(userId2);

		Collections.reverse(all1); // because ordered by date desc
		Collections.reverse(all2); // because ordered by date desc

		assert all1.size() == saved1.length;
		assert all2.size() == saved2.length;

		for (int i = 0; i < saved1.length; i++) assertEquals(all1.get(i), saved1[i]);
		for (int i = 0; i < saved2.length; i++) assertEquals(all2.get(i), saved2[i]);

		notificationDao.removeUserNotifications(userId1);

		assert notificationDao.countUserNotifications(userId1) == 0;
		assert notificationDao.getAllNotificationsByUserIdOrderDesc(userId1).size() == 0;
	}


}