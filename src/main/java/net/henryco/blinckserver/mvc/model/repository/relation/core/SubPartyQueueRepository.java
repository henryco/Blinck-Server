package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.SubPartyQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 15/09/17.
 */
public interface SubPartyQueueRepository extends JpaRepository<SubPartyQueue, Long> {

	void removeAllByOwner(Long owner);
	List<SubPartyQueue> getAllByUsersIsContainingOrOwner(Long user, Long owner);

}