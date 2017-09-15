package net.henryco.blinckserver.mvc.model.dao.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.SubPartyQueue;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 15/09/17.
 */
public interface SubPartyQueueDao extends BlinckDaoTemplate<SubPartyQueue, Long> {

	void deleteAllByOwnerId(Long id);
	List<SubPartyQueue> getAllWithUser(Long user);
}