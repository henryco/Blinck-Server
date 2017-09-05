package net.henryco.blinckserver.mvc.service.relation.conversation;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.FriendshipConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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



	private FriendshipConversationDao getDao() {
		return provideDao();
	}



	@Transactional
	public List<FriendshipConversation> getByFriendshipId(Long id, int page, int size) {
		return getDao().getByFriendshipId(id, page, size);
	}


	@Transactional
	public List<FriendshipConversation> getByFriendshipId(Long id) {
		return getDao().getByFriendshipId(id);
	}


	@Transactional
	public void deleteAllByFriendshipId(Long id) {
		getDao().deleteByFriendshipId(id);
	}


	@Transactional
	public long countByFriendshipId(Long id) {
		return getDao().countByFriendshipId(id);
	}

}