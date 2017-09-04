package net.henryco.blinckserver.mvc.model.repository.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 04/09/17.
 */
public interface FriendshipNotificationRepository extends JpaRepository<FriendshipNotification, Long> {

	List<FriendshipNotification> getAllByInitiatorIdOrReceiverIdOrderByDateDesc(Long initiatorId, Long receiverId, Pageable pageable);
	List<FriendshipNotification> getAllByInitiatorIdOrderByDateDesc(Long initiatorId, Pageable pageable);
	List<FriendshipNotification> getAllByReceiverIdOrderByDateDesc(Long receiverId, Pageable pageable);

	FriendshipNotification getByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId
			(Long receiverId, Long initiatorId, Long initiatorId2, Long receiverId2);

	boolean existsByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId(Long receiverId, Long initiatorId, Long initiatorId2, Long receiverId2);

	void removeAllByReceiverIdAndInitiatorIdOrInitiatorIdAndReceiverId(Long receiverId, Long initiatorId, Long initiatorId2, Long receiverId2);
	void removeAllByInitiatorIdOrReceiverId(Long initiatorId, Long receiverId);
	void removeAllByInitiatorId(Long initiatorId);
	void removeAllByReceiverId(Long receiverId);

}