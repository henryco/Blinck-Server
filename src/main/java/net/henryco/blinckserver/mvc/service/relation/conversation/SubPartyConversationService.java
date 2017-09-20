package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class SubPartyConversationService
		extends BlinckDaoProvider<SubPartyConversation, Long>
		implements ConversationService<SubPartyConversation> {



	public SubPartyConversationService(SubPartyConversationDao conversationDao) {
		super(((conversationDao)));
	}

	private SubPartyConversationDao getDao() {
		return provideDao();
	}


	@Override @Transactional
	public SubPartyConversation sendMessage(SubPartyConversation conversation) {
		return getDao().save(conversation);
	}


	@Override @Transactional
	public SubPartyConversation sendMessage(MessageForm messageForm, Long userId) {

		SubPartyConversation conversation = new SubPartyConversation();
		conversation.setSubParty(messageForm.getTopic());
		conversation.setMessagePart(messageForm.getMessagePart(userId));
		return sendMessage(conversation);
	}


	@Override @Transactional
	public MessageForm[] getLastNByTopic(Long subPartyId, int page, int size) {

		return getDao().getLastNBySubPartyId(subPartyId, page, size)
				.stream().map(MessageForm::new)
		.toArray(MessageForm[]::new);
	}


	@Override @Transactional
	public void deleteAllInTopic(Long subPartyId) {
		getDao().deleteAllBySubPartyId(subPartyId);
	}


	@Override @Transactional
	public long countByTopic(Long subPartyId) {
		return getDao().countBySubPartyId(subPartyId);
	}

}
