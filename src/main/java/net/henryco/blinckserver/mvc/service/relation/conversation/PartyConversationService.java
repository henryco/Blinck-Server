package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.PartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class PartyConversationService
		extends BlinckDaoProvider<PartyConversation, Long>
		implements ConversationService<PartyConversation>{


	public PartyConversationService(PartyConversationDao partyConversationDao) {
		super(((partyConversationDao)));
	}

	private PartyConversationDao getDao() {
		return provideDao();
	}


	@Override @Transactional
	public PartyConversation sendMessage(PartyConversation message) {
		return getDao().save(message);
	}


	@Override @Transactional
	public PartyConversation sendMessage(MessageForm message, Long userId) {

		PartyConversation conversation = new PartyConversation();
		conversation.setParty(message.getTopic());
		conversation.setMessagePart(message.getMessagePart(userId));
		return sendMessage(conversation);
	}


	@Override @Transactional
	public long countByTopic(Long topicId) {
		return getDao().countByParty(topicId);
	}


	@Override @Transactional
	public void deleteAllInTopic(Long topicId) {
		getDao().deleteAllForParty(topicId);
	}


	@Override @Transactional
	public MessageForm[] getLastNByTopic(Long topicId, int page, int size) {
		return getDao().getLastByParty(topicId, page, size)
				.stream().map(MessageForm::new)
		.toArray(MessageForm[]::new);
	}

}