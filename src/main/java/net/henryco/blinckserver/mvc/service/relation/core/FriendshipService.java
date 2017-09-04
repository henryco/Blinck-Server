package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Henry on 03/09/17.
 */ @Service
public class FriendshipService {

	private final FriendshipDao friendshipDao;

	@Autowired
	public FriendshipService(FriendshipDao friendshipDao) {
		this.friendshipDao = friendshipDao;
	}


	/**
	 * Create and save new friendship relation between users.
	 * @return <b>ID</b> of saved relation id database.
	 */ @Transactional
	public Long addFriendshipRelation(FriendshipNotification notification) {

		Friendship friendship = new Friendship();
		friendship.setUser1(notification.getInitiatorId());
		friendship.setUser2(notification.getReceiverId());
		friendship.setDate(new Date(System.currentTimeMillis()));
		return friendshipDao.save(friendship).getId();
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
		return friendshipDao.getAllByUserIdOrderByDateDesc(user);
	}


	@Transactional
	public List<Friendship> getAllUserRelations(Long user, int page, int size) {
	 	return friendshipDao.getAllByUserIdOrderByDateDesc(user, page, size);
	}


	/**
	 * Delete <b>ALL</b> friendship relations with user.
	 */ @Transactional
	public void deleteAllUserRelations(Long user) {
		friendshipDao.deleteAllByUserId(user);
	}


	/**
	 * Delete friendship relation between users.<br>
	 * <b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public void deleteRelationBetweenUsers(Long user1, Long user2) {
		friendshipDao.deleteRelationBetweenUsers(user1, user2);
	}


	@Transactional
	public Friendship getById(Long id) {
		return friendshipDao.getById(id);
	}


	/**
	 *<b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public Friendship getByUsers(Long user1, Long user2) {
	 	return friendshipDao.getByUsers(user1, user2);
	}


	/**
	 *<b>Arguments order doesn't matter.</b>
	 */ @Transactional
	public boolean isExistsBetweenUsers(Long user1, Long user2) {
		return friendshipDao.isRelationBetweenUsersExists(user1, user2);
	}


	@Transactional
	public boolean isExistsById(Long id) {
	 	return friendshipDao.isExists(id);
	}


	@Transactional
	public void deleteById(Long id) {
	 	friendshipDao.deleteById(id);
	}

}