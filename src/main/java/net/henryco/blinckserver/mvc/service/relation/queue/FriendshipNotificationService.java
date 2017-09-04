package net.henryco.blinckserver.mvc.service.relation.queue;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Henry on 04/09/17.
 */ @Service
public class FriendshipNotificationService {

	private final FriendshipNotificationDao friendshipNotificationDao;

	@Autowired
	public FriendshipNotificationService(FriendshipNotificationDao friendshipNotificationDao) {
		this.friendshipNotificationDao = friendshipNotificationDao;
	}



	/**
	 * @return <b>Notification ID</b> or <b>NULL</b> if initiator and receiver is the same person.
	 */ @Transactional
	public Long addNotification(Long initiator, Long receiver) {

		if (initiator.equals(receiver)) return null;
	 	FriendshipNotification notification = new FriendshipNotification();
		notification.setInitiatorId(initiator);
		notification.setReceiverId(receiver);
		notification.setDate(new Date(System.currentTimeMillis()));
		return friendshipNotificationDao.save(notification).getId();
	}


	@Transactional
	public List<FriendshipNotification> getAllNotificationByInitiator(Long initiator, int page, int size) {
		return friendshipNotificationDao.getAllByInitiatorId(initiator, page, size);
	}


	@Transactional
	public List<FriendshipNotification> getAllNotificationByReceiver(Long receiver, int page, int size) {
		return friendshipNotificationDao.getAllByReceiverId(receiver, page, size);
	}


	@Transactional
	public List<FriendshipNotification> getAllWithUser(Long user, int page, int size) {
		return friendshipNotificationDao.getAllByUserId(user, page, size);
	}


	@Transactional
	public List<FriendshipNotification> getAll() {
	 	return friendshipNotificationDao.getAll();
	}


	@Transactional
	public FriendshipNotification getById(Long id) {
		return friendshipNotificationDao.getById(id);
	}


	/**
	 * <b>Order doesn't matter.</b>
	 */ @Transactional
	public FriendshipNotification getWithUsers(Long user1, Long user2) {
	 	return friendshipNotificationDao.getByReceiverAndInitiator(user1, user2);
	}


	/**
	 * <b>Order doesn't matter.</b>
	 */ @Transactional
	public boolean isExistsBetweenUsers(Long user1, Long user2) {
		return friendshipNotificationDao.existsBetweenReceiverAndInitiator(user1, user2);
	}


	@Transactional
	public boolean isExists(Long notificationId) {
	 	return friendshipNotificationDao.isExists(notificationId);
	}


	@Transactional
	public void deleteById(Long id) {
		friendshipNotificationDao.deleteById(id);
	}


	@Transactional
	public void deleteAllByUser(Long userId) {
		friendshipNotificationDao.removeAllByUserId(userId);
	}


	/**
	 * Delete notification with users.<br>
	 * <b>Order doesn't matter.</b>
	 */ @Transactional
	public void deleteByUsers(Long user1, Long user2) {
		friendshipNotificationDao.removeAllByReceiverAndInitiatorId(user1, user2);
	}


}