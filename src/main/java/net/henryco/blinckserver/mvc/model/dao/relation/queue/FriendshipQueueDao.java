package net.henryco.blinckserver.mvc.model.dao.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipQueue;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 04/09/17.
 */
public interface FriendshipQueueDao extends BlinckDaoTemplate<FriendshipQueue, Long> {

	List<FriendshipQueue> getAllByReceiverId(Long receiverId, int page, int size);
	List<FriendshipQueue> getAllByInitiatorId(Long initiatorId, int page, int size);
	List<FriendshipQueue> getAllByUserId(Long id, int page, int size);

	boolean existsBetweenReceiverAndInitiator(Long receiverId, Long initiatorId);

	void removeAllByReceiverAndInitiatorId(Long receiverId, Long initiatorId);
	void removeAllByReceiverId(Long receiverId);
	void removeAllByInitiatorId(Long initiatorId);
	void removeAllByUserId(Long id);
}