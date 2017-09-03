package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipQueueDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @author Henry on 03/09/17.
 */
@Service
public class FriendshipService {

	private final FriendshipDao friendshipDao;
	private final FriendshipQueueDao friendshipQueueDao;


	@Autowired
	public FriendshipService(FriendshipDao friendshipDao,
							 FriendshipQueueDao friendshipQueueDao) {
		this.friendshipDao = friendshipDao;
		this.friendshipQueueDao = friendshipQueueDao;
	}




	/**
	 * Create and save new friendship relation between users.
	 * @return <b>ID</b> of saved relation id database.
	 */
	@Transactional
	public Long saveFriendshipRelation(Long user1, Long user2) {

		Friendship friendship = new Friendship();
		friendship.setUser1(user1);
		friendship.setUser2(user2);
		friendship.setDate(new Date(System.currentTimeMillis()));
		return friendshipDao.save(friendship).getId();
	}


	@Transactional
	public List<Friendship> getAllUserRelations(Long user) {
		return friendshipDao.getAllByUserIdOrderByDateDesc(user);
	}


	/**
	 * Delete <b>ALL</b> friendship relations with user.
	 */
	@Transactional
	public void deleteAllUserRelations(Long user) {
		friendshipDao.deleteAllByUserId(user);
	}


	/**
	 * Delete friendship relation between users.<br>
	 * <b>Arguments order doesn't matter.</b>
	 */
	@Transactional
	public void deleteRelationBetweenUsers(Long user1, Long user2) {
		friendshipDao.deleteAllWithUsers(user1, user2);
	}


}