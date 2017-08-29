package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class FriendshipConversationDaoImp implements FriendshipConversationDao {
	@Override
	public FriendshipConversation getById(Long id) {
		return null;
	}

	@Override
	public FriendshipConversation save(FriendshipConversation friendshipConversation) {
		return null;
	}

	@Override
	public boolean isExists(Long id) {
		return false;
	}

	@Override
	public void deleteById(Long id) {

	}
}