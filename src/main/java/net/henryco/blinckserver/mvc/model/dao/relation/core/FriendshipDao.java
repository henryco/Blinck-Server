package net.henryco.blinckserver.mvc.model.dao.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
public interface FriendshipDao extends BlinckDaoTemplate<Friendship, Long> {

	List<Friendship> getAllByUserIdOrderByDateDesc(Long userId, int page, int size);
	List<Friendship> getAllByUserIdOrderByDateDesc(Long userId);

	Friendship getByUsers(Long user1, Long user2);

	Long countByUserId(Long userId);

	boolean isRelationBetweenUsersExists(Long user1, Long user2);

	void deleteAllByUserId(Long userId);
	void deleteRelationBetweenUsers(Long user1, Long user2);

}