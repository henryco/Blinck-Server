package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.model.repository.relation.conversation.FriendshipConversationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class FriendshipConversationDaoImp
		extends BlinckRepositoryProvider<FriendshipConversation, Long>
		implements FriendshipConversationDao {

	public FriendshipConversationDaoImp(FriendshipConversationRepository repository) {
		super(repository);
	}

}