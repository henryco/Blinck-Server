package net.henryco.blinckserver.mvc.model.dao.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.PartyMeetingOffer;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 18/09/17.
 */
public interface PartyMeetingOfferDao extends BlinckDaoTemplate<PartyMeetingOffer, Long> {

	List<PartyMeetingOffer> getMeetingOfferListByPartyId(Long partyId);
	List<PartyMeetingOffer> getAllWithUserAndPartyId(Long partyId, Long userId);
	void deleteAllByParty(Long party);

}
