package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.profile.core.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Henry on 03/09/17.
 */
public class FriendShipEntityTest extends UserEntityIntegrationTest {


	private @Autowired FriendshipDao friendshipDao;


	@Test @Transactional
	public void addFriendshipTest() {

		Long[] users = saveNewRandomUsers(this);

		UserCoreProfile user1 = coreProfileDao.getById(users[0]);
		UserCoreProfile user2 = coreProfileDao.getById(users[1]);

		assert user1 != null;
		assert user2 != null;

		Friendship friendship = new Friendship();
		friendship.setDate(new Date(System.currentTimeMillis()));
		friendship.setUser1(user1.getId());
		friendship.setUser2(user2.getId());

		Long id = friendshipDao.save(friendship).getId();

		Friendship saved = friendshipDao.getById(id);
		assert saved != null;
		assert saved.getUser1().equals(users[0]);
		assert saved.getUser2().equals(users[1]);
	}


	@Test @Transactional
	public void findAllRelationsTest() {



	}


}