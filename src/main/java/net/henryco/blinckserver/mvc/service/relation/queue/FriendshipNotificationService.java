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
	 * @return Notification ID
	 */ @Transactional
	public Long addNotification(Long initiator, Long receiver) {

		FriendshipNotification notification = new FriendshipNotification();
		notification.setInitiatorId(initiator);
		notification.setReceiverId(receiver);
		notification.setDate(new Date(System.currentTimeMillis()));
		return friendshipNotificationDao.save(notification).getId();
	}


	@Transactional
	public List<FriendshipNotification> getAllNotificationFromInitiator(Long initiator, int page, int size) {
		return friendshipNotificationDao.getAllByInitiatorId(initiator, page, size);
	}


	@Transactional
	public List<FriendshipNotification> getAllNotificationFromReceiver(Long receiver, int page, int size) {
		return friendshipNotificationDao.getAllByReceiverId(receiver, page, size);
	}


	@Transactional
	public List<FriendshipNotification> getAllWithUser(Long user, int page, int size) {
		return friendshipNotificationDao.getAllByUserId(user, page, size);
	}


	@Transactional
	public FriendshipNotification getById(Long id) {
		return friendshipNotificationDao.getById(id);
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