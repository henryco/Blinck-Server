package net.henryco.blinckserver.mvc.model.dao.relation.queue.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.FriendshipNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import net.henryco.blinckserver.mvc.model.repository.relation.queue.FriendshipNotificationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Henry on 04/09/17.
 */
@Repository
public class FriendshipNotificationDaoImp
		extends BlinckRepositoryProvider<FriendshipNotification, Long>
		implements FriendshipNotificationDao {


	@Autowired
	public FriendshipNotificationDaoImp(FriendshipNotificationRepository repository) {
		super(repository);
	}

	private FriendshipNotificationRepository getRepository() {
		return provideRepository();
	}


	@Override
	public List<FriendshipNotification> getAllByReceiverId(Long receiverId, int page, int size) {
		return getRepository().getAllByReceiverIdOrderByDateDesc(receiverId, new PageRequest(page, size));
	}


	@Override
	public List<FriendshipNotification> getAllByInitiatorId(Long initiatorId, int page, int size) {
		return getRepository().getAllByInitiatorIdOrderByDateDesc(initiatorId, new PageRequest(page, size));
	}


	@Override
	public List<FriendshipNotification> getAllByUserId(Long id, int page, int size) {
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

	@Override
	public FriendshipNotification getByReceiverAndInitiator(Long receiverId, Long initiatorId) {
		return getRepository().getByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId(receiverId, initiatorId, receiverId, initiatorId);
	}
}