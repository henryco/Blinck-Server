package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.model.repository.relation.conversation.FriendshipConversationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

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


	private FriendshipConversationRepository getRepository() {
		return provideRepository();
	}


	@Override
	public void deleteByFriendshipId(Long id) {
		getRepository().removeAllByFriendship(id);
	}


	@Override
	public List<FriendshipConversation> getByFriendshipId(Long id, int page, int size) {
		return getRepository().getAllByFriendshipOrderByMessagePart_Date_Desc(id, new PageRequest(page, size));
	}

	@Override
	public long countByFriendshipId(Long id) {
		return getRepository().countAllByFriendship(id);
	}
}