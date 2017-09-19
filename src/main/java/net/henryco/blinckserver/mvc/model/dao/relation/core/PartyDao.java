package net.henryco.blinckserver.mvc.model.dao.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

/**
 * @author Henry on 29/08/17.
 */
public interface PartyDao extends BlinckDaoTemplate<Party, Long> {

	Party getRandomFirstInQueue(String typeWanted, String typeIdent, Integer dimension);
	Boolean isPartyActive(Long partyId);

}