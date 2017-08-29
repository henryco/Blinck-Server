package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class SubPartyConversationDaoImp implements SubPartyConversationDao {
	@Override
	public SubPartyConversation getById(Long id) {
		return null;
	}

	@Override
	public SubPartyConversation save(SubPartyConversation subPartyConversation) {
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