package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.PartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class PartyConversationDaoImp implements PartyConversationDao {
	@Override
	public PartyConversation getById(Long id) {
		return null;
	}

	@Override
	public PartyConversation save(PartyConversation partyConversation) {
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