package net.henryco.blinckserver.mvc.service.relation.queue;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipQueueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 04/09/17.
 */
@Service
public class FriendshipQueueService {

	private final FriendshipQueueDao friendshipQueueDao;

	@Autowired
	public FriendshipQueueService(FriendshipQueueDao friendshipQueueDao) {
		this.friendshipQueueDao = friendshipQueueDao;
	}

	// TODO: 04/09/17



}