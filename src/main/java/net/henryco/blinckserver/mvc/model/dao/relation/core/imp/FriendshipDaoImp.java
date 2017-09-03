package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.FriendshipDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.model.repository.relation.core.FriendshipRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class FriendshipDaoImp extends
		BlinckRepositoryProvider<Friendship, Long>
		implements FriendshipDao {


	public FriendshipDaoImp(FriendshipRepository repository) {
		super(repository);
	}

	private FriendshipRepository getRepository() {
		return provideRepository();
	}

	@Override
	public List<Friendship> getAllByUserIdOrderByDateDesc(Long userId) {
		return getRepository().getAllByUser1OrUser2OrderByDateDesc(userId, userId);
	}

	@Override
	public void deleteAllByUserId(Long userId) {
		getRepository().removeAllByUser1OrUser2(userId, userId);
	}

	@Override
	public void deleteAllWithUsers(Long user1, Long user2) {
		getRepository().removeAllByUser1AndUser2OrUser2AndUser1(user1, user2, user1, user2);
	}
}