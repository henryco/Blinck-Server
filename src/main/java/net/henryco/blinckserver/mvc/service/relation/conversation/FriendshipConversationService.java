package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Henry on 03/09/17.
 */
@Service
public class FriendshipConversationService
		extends BlinckDaoProvider<FriendshipConversation, Long>
		implements ConversationService<FriendshipConversation> {

	@Autowired
	public FriendshipConversationService(FriendshipConversationDao daoTemplate) {
		super(daoTemplate);
	}


	private FriendshipConversationDao getDao() {
		return provideDao();
	}



	@Override @Transactional
	public FriendshipConversation sendMessage(FriendshipConversation message) {
		return getDao().save(message);
	}

	@Override @Transactional
	public FriendshipConversation sendMessage(MessageForm message, Long userId) {

		FriendshipConversation conversation = new FriendshipConversation();
		conversation.setFriendship(message.getTopic());
		conversation.setMessagePart(message.getMessagePart(userId));
		return sendMessage(conversation);
	}


	@Override @Transactional
	public long countByTopic(Long topicId) {
		return getDao().countByFriendshipId(topicId);
	}

	@Override @Transactional
	public void deleteAllInTopic(Long topicId) {
		getDao().deleteByFriendshipId(topicId);
	}

	@Override @Transactional
	public MessageForm[] getLastNByTopic(Long topicId, int page, int size) {
		return getDao().getByFriendshipId(topicId, page, size)
				.stream().map(MessageForm::new)
		.toArray(MessageForm[]::new);
	}

}