package net.henryco.blinckserver.mvc.model.dao.relation.queue.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipQueueDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipQueue;
import net.henryco.blinckserver.mvc.model.repository.relation.queue.FriendshipQueueRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 04/09/17.
 */
@Repository
public class FriendshipQueueDaoImp
		extends BlinckRepositoryProvider<FriendshipQueue, Long>
		implements FriendshipQueueDao {


	@Autowired
	public FriendshipQueueDaoImp(FriendshipQueueRepository repository) {
		super(repository);
	}

	private FriendshipQueueRepository getRepository() {
		return provideRepository();
	}





}