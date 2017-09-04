package net.henryco.blinckserver.mvc.model.dao.relation.queue.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipQueueDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipQueue;
import net.henryco.blinckserver.mvc.model.repository.relation.queue.FriendshipQueueRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

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


	@Override
	public List<FriendshipQueue> getAllByReceiverId(Long receiverId, int page, int size) {
		return getRepository().getAllByReceiverIdOrderByDateDesc(receiverId, new PageRequest(page, size));
	}


	@Override
	public List<FriendshipQueue> getAllByInitiatorId(Long initiatorId, int page, int size) {
		return getRepository().getAllByInitiatorIdOrderByDateDesc(initiatorId, new PageRequest(page, size));
	}


	@Override
	public List<FriendshipQueue> getAllByUserId(Long id, int page, int size) {
		return getRepository().getAllByInitiatorIdOrReceiverIdOrderByDateDesc(id, id, new PageRequest(page, size));
	}


	@Override
	public void removeAllByReceiverId(Long receiverId) {
		getRepository().removeAllByReceiverId(receiverId);
	}


	@Override
	public void removeAllByInitiatorId(Long initiatorId) {
		getRepository().removeAllByInitiatorId(initiatorId);
	}


	@Override
	public void removeAllByUserId(Long id) {
		getRepository().removeAllByInitiatorIdOrReceiverId(id, id);
	}


	@Override
	public void removeAllByReceiverAndInitiatorId(Long receiverId, Long initiatorId) {
		getRepository().removeAllByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId(receiverId, initiatorId, receiverId, initiatorId);
	}

	@Override
	public boolean existsBetweenReceiverAndInitiator(Long receiverId, Long initiatorId) {
		return getRepository().existsByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId(receiverId, initiatorId, receiverId, initiatorId);
	}

}