package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.PartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class PartyConversationService
		extends BlinckDaoProvider<PartyConversation, Long> {


	public PartyConversationService(PartyConversationDao partyConversationDao) {
		super(((partyConversationDao)));
	}

	private PartyConversationDao getDao() {
		return provideDao();
	}

}