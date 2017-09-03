package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 03/09/17.
 */
@Service
public class FriendshipConversationService
		extends BlinckDaoProvider<FriendshipConversation, Long> {

	@Autowired
	public FriendshipConversationService(FriendshipConversationDao daoTemplate) {
		super(daoTemplate);
	}


}