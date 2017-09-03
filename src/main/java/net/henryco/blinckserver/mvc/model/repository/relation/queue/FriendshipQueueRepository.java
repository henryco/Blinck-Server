package net.henryco.blinckserver.mvc.model.repository.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipQueue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 04/09/17.
 */
public interface FriendshipQueueRepository extends JpaRepository<FriendshipQueue, Long> {


}