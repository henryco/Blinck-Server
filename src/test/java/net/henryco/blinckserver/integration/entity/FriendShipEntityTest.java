package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Henry on 03/09/17.
 */
public class FriendShipEntityTest extends BlinckUserIntegrationTest {


	private @Autowired FriendshipDao friendshipDao;



	@Test @Transactional
	public void addFriendshipTest() {

		Long[] users = saveNewRandomUsers(this);

		UserCoreProfile user1 = coreProfileDao.getById(users[0]);
		UserCoreProfile user2 = coreProfileDao.getById(users[1]);

		assert user1 != null;
		assert user2 != null;

		Long id = saveFriendship(this, user1.getId(), user2.getId());
		Friendship saved = friendshipDao.getById(id);

		assert saved != null;
		assert saved.getUser1().equals(users[0]);
		assert saved.getUser2().equals(users[1]);
	}



	@Test @Transactional
	public void findAllRelationsTest() {

		Long[] users = saveNewRandomUsers(this, 5);
		Long id1 = saveFriendship(this, users[0], users[1]);
		Long id2 = saveFriendship(this, users[0], users[2]);
		Long id3 = saveFriendship(this, users[3], users[0]);
		Long id4 = saveFriendship(this, users[2], users[4]);
		Long id5 = saveFriendship(this, users[2], users[1]);

		List<Friendship> all = friendshipDao.getAllByUserIdOrderByDateDesc(users[0]);
		assert all.size() == 3;
		assert all.get(0).getId().equals(id3);
		assert all.get(1).getId().equals(id2);
		assert all.get(2).getId().equals(id1);

		List<Friendship> rest = friendshipDao.getAllByUserIdOrderByDateDesc(users[2]);
		assert rest.size() == 3;
		assert rest.get(0).getId().equals(id5);
		assert rest.get(1).getId().equals(id4);
		assert rest.get(2).getId().equals(id2);
	}



	@Test @Transactional
	public void findUserFromRelationTest() {

		Long[] users = saveNewRandomUsers(this, 8);
		saveFriendship(this, users[1], users[4]);
		saveFriendship(this, users[3], users[5]);
		saveFriendship(this, users[2], users[1]);
		saveFriendship(this, users[7], users[6]);
		saveFriendship(this, users[1], users[6]);

		friendshipDao.getAllByUserIdOrderByDateDesc(users[1]).forEach(friendship -> {
			assert coreProfileDao.isExists(friendship.getUser1());
			assert coreProfileDao.isExists(friendship.getUser2());
		});
	}



	@Test @Transactional
	public void deleteRelationsTestOne() {

		Long[] users = saveNewRandomUsers(this, 3);
		saveFriendship(this, users[0], users[1]);
		saveFriendship(this, users[0], users[2]);
		saveFriendship(this, users[2], users[1]);

		friendshipDao.getAllByUserIdOrderByDateDesc(users[1]).forEach(friendship ->
				friendshipDao.deleteById(friendship.getId())
		);

		assertionDelete(this, users);
	}



	@Test @Transactional
	public void deleteRelationsTestTwo() {

		Long[] users = saveNewRandomUsers(this, 3);
		saveFriendship(this, users[0], users[1]);
		saveFriendship(this, users[0], users[2]);
		saveFriendship(this, users[2], users[1]);

		assert friendshipDao.isRelationBetweenUsersExists(users[1], users[2]);

		friendshipDao.deleteAllByUserId(users[1]);

		assert !friendshipDao.isRelationBetweenUsersExists(users[1], users[2]);
		assertionDelete(this, users);
	}



	@Test @Transactional
	public void deleteSingleRelationTest() {

		Long[] users = saveNewRandomUsers(this, 2);

		saveFriendship(this, users[0], users[1]);
		assert friendshipDao.getAllByUserIdOrderByDateDesc(users[0]).size() == 1;

		friendshipDao.deleteRelationBetweenUsers(users[1], users[0]);
		assert friendshipDao.getAllByUserIdOrderByDateDesc(users[0]).size() == 0;

		saveFriendship(this, users[1], users[0]);
		assert friendshipDao.getAllByUserIdOrderByDateDesc(users[0]).size() == 1;

		friendshipDao.deleteRelationBetweenUsers(users[1], users[0]);
		assert friendshipDao.getAllByUserIdOrderByDateDesc(users[0]).size() == 0;
	}


	@Test @Transactional
	public void relationExistenceTest() {

		Long[] users = saveNewRandomUsers(this, 2);

		saveFriendship(this, users[0], users[1]);
		assert friendshipDao.isRelationBetweenUsersExists(users[1], users[0]);
		assert friendshipDao.isRelationBetweenUsersExists(users[0], users[1]);

		friendshipDao.deleteRelationBetweenUsers(users[1], users[0]);
		assert !friendshipDao.isRelationBetweenUsersExists(users[1], users[0]);
		assert !friendshipDao.isRelationBetweenUsersExists(users[0], users[1]);
	}



	private @SuppressWarnings("StatementWithEmptyBody") static Long
	saveFriendship(FriendShipEntityTest context, Long id1, Long id2) {

		Friendship friendship = new Friendship();
		friendship.setDate(new Date(System.currentTimeMillis()));

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		friendship.setUser1(id1);
		friendship.setUser2(id2);
		return context.friendshipDao.save(friendship).getId();
	}


	private static void
	assertionDelete(FriendShipEntityTest context, Long[] users) {

		assert context.friendshipDao.getAllByUserIdOrderByDateDesc(users[1]).size() == 0;
		assert context.friendshipDao.getAllByUserIdOrderByDateDesc(users[0]).size() == 1;
		assert context.friendshipDao.getAllByUserIdOrderByDateDesc(users[2]).size() == 1;

		assert context.coreProfileDao.isExists(users[0]);
		assert context.coreProfileDao.isExists(users[1]);
		assert context.coreProfileDao.isExists(users[2]);
	}

}