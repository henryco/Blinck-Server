package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class SubPartyConversationService
		extends BlinckDaoProvider<SubPartyConversation, Long> {

	public SubPartyConversationService(SubPartyConversationDao conversationDao) {
		super(((conversationDao)));
	}

	private SubPartyConversationDao getDao() {
		return provideDao();
	}


}
