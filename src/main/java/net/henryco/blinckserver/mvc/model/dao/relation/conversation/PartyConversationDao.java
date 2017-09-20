package net.henryco.blinckserver.mvc.model.dao.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
public interface PartyConversationDao extends BlinckDaoTemplate<PartyConversation, Long> {

	List<PartyConversation> getLastByParty(Long partyId, int page, int size);
	long countByParty(Long partyId);
	void deleteAllForParty(Long partyId);
}
