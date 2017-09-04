package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.UserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.mvc.service.relation.queue.FriendshipNotificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 04/09/17.
 */
public class FriendshipSuperServiceTest extends UserIntegrationTest {


	private @Autowired FriendshipService friendshipService;
	private @Autowired FriendshipNotificationService notificationService;



	@Test @Transactional
	public void createNotificationTest() {

		final Long[] users = saveNewRandomUsers(this, 2);
		Long notification = notificationService.addNotification(users[0], users[1]);

		List<FriendshipNotification> all = notificationService.getAll();
		assert all.stream().anyMatch(n -> notification.equals(n.getId()));

		List<FriendshipNotification> withUser = notificationService.getAllWithUser(users[0], 0, 10);
		assert withUser.size() == 1;
		assert withUser.stream().anyMatch(n -> n.getId().equals(notification));

		notificationService.deleteByUsers(users[1], users[0]);
		assert notificationService.getAllWithUser(users[0], 0, 10).size() == 0;
	}


	@Test @Transactional
	public void createFriendshipTest() {

		final Long[] users = saveNewRandomUsers(this, 2);
		notificationService.addNotification(users[1], users[0]);

		List<FriendshipNotification> all = notificationService.getAllNotificationFromReceiver(users[0], 0, 10);
		assert all.size() == 1;

		FriendshipNotification notification = all.get(0);

		Long relation = friendshipService.addFriendshipRelation(notification);

		notificationService.deleteByUsers(users[1], users[0]);
		List<Friendship> byUser1 = friendshipService.getAllUserRelations(users[0]);
		List<Friendship> byUser2 = friendshipService.getAllUserRelations(users[1]);
		Friendship byId = friendshipService.getById(relation);

		assert byUser1.size() == 1;
		assert byUser2.size() == 1;

		assert byUser1.get(0).equals(byUser2.get(0));
		assert byId.equals(byUser1.get(0));
	}


	@Test @Transactional
	public void manyNotificationsTest() throws InterruptedException {

		final Long[] users = saveNewRandomUsers(this, 8);
		for (Long user: users) {
			notificationService.addNotification(user, users[0]);
			Thread.sleep(1);
		}

		assert notificationService.getAllNotificationFromInitiator(users[1], 0, 10).size() == 1;

		List<FriendshipNotification> all = notificationService.getAllNotificationFromReceiver(users[0], 0, 100);

		assert all.size() == 7;
		assert all.get(0).getInitiatorId().equals(users[7]);
		assert all.get(1).getInitiatorId().equals(users[6]);
		assert all.get(2).getInitiatorId().equals(users[5]);
		assert all.get(3).getInitiatorId().equals(users[4]);
		assert all.get(4).getInitiatorId().equals(users[3]);
		assert all.get(5).getInitiatorId().equals(users[2]);
		assert all.get(6).getInitiatorId().equals(users[1]);

		notificationService.deleteByUsers(users[2], users[0]);
		assert notificationService.getAllWithUser(users[0], 0, 100).size() == 6;

		notificationService.deleteAllByUser(users[0]);
		assert notificationService.getAllWithUser(users[0], 0, 100).size() == 0;
	}


	@Test @Transactional
	public void pureFriendshipCreationTest() {

		final Long[] users = saveNewRandomUsers(this, 2);
		Long relation = friendshipService.addFriendshipRelation(users[0], users[1]);

		assert friendshipService.isExistsBetweenUsers(users[0], users[1]);
		assert friendshipService.isExistsBetweenUsers(users[1], users[0]);
		assert friendshipService.isExistsById(relation);
	}
}