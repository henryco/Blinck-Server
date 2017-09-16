package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Henry on 03/09/17.
 */ @Service
public class FriendshipService
		extends BlinckDaoProvider<Friendship, Long> {



	@Autowired
	public FriendshipService(FriendshipDao friendshipDao) {
		super(friendshipDao);
	}

	private FriendshipDao getDao() {
		return provideDao();
	}



	/**
	 * Create and save new friendship relation between users.
	 * @return <b>ID</b> of saved relation id database.
	 */ @Transactional
	public Long addFriendshipRelation(FriendshipNotification notification) {

	 	if (!notificationFilter(notification)) return null;

		Friendship friendship = new Friendship();
		friendship.setUser1(notification.getInitiatorId());
		friendship.setUser2(notification.getReceiverId());
		friendship.setDate(new Date(System.currentTimeMillis()));
		return getDao().save(friendship).getId();
	}


	/**
	 * Create and save new friendship relation between users.
	 * @return <b>ID</b> of saved relation id database.
	 */ @Transactional
	public Long addFriendshipRelation(Long initiator, Long receiver) {

	 	FriendshipNotification notification = new FriendshipNotification();
		notification.setInitiatorId(initiator);
		notification.setReceiverId(receiver);
		return addFriendshipRelation(notification);
	}



	@Transactional
	public List<Friendship> getAllUserRelations(Long user) {
		return getDao().getAllByUserIdOrderByDateDesc(user);
	}



	@Transactional
	public List<Friendship> getAllUserRelations(Long user, int page, int size) {
	 	return getDao().getAllByUserIdOrderByDateDesc(user, page, size);
	}



	/**
	 * Delete <b>ALL</b> friendship relations with user.
	 */ @Transactional
	public void deleteAllUserRelations(Long user) {
		getDao().deleteAllByUserId(user);
	}



	/**
	 * Delete friendship relation between users.<br>
	 * <b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public void deleteRelationBetweenUsers(Long user1, Long user2) {
		getDao().deleteRelationBetweenUsers(user1, user2);
	}



	/**
	 *<b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public Friendship getByUsers(Long user1, Long user2) {
	 	return getDao().getByUsers(user1, user2);
	}



	/**
	 *<b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public boolean isExistsBetweenUsers(Long user1, Long user2) {
		return getDao().isRelationBetweenUsersExists(user1, user2);
	}



	@Transactional
	public Long getFriendsCount(Long id) {
	 	return getDao().countByUserId(id);
	}



	@Transactional
	public boolean existsRelationWithUser(Long friendshipId, Long userId) {

		if (getDao().isExists(friendshipId)) {
			Friendship friendship = getDao().getById(friendshipId);
			return friendship.getUser1().equals(userId) ||
					friendship.getUser2().equals(userId);
		}
	 	return false;
	}


	@Transactional
	public Long getSecondUser(Long friendshipId, Long userId) {

	 	Friendship friendship = getDao().getById(friendshipId);
		return friendship.getUser1().equals(userId)
				? friendship.getUser2()
				: friendship.getUser1();
	}


	@Transactional
	protected boolean notificationFilter(FriendshipNotification notification) {
		return !notification.getInitiatorId().equals(notification.getReceiverId())
				&& !isExistsBetweenUsers(notification.getInitiatorId(), notification.getReceiverId());
	}

}