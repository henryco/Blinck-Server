package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @author Henry on 03/09/17.
 */
@Service
public class FriendshipService
		extends BlinckDaoProvider<Friendship, Long> {


	@Autowired
	public FriendshipService(FriendshipDao daoTemplate) {
		super(daoTemplate);
	}

	private FriendshipDao getDao() {
		return provideDao();
	}


	@Transactional
	public Long saveFriendshipRelation(Long user1, Long user2) {

		Friendship friendship = new Friendship();
		friendship.setUser1(user1);
		friendship.setUser2(user2);
		friendship.setDate(new Date(System.currentTimeMillis()));
		return getDao().save(friendship).getId();
	}


	@Transactional
	public List<Friendship> getAllUserRelations(Long user) {
		return getDao().getAllByUserIdOrderByDateDesc(user);
	}


	public void deleteAllUserRelations(Long user) {

	}


}