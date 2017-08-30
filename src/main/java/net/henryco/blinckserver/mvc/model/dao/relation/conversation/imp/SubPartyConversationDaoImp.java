package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import net.henryco.blinckserver.mvc.model.repository.relation.conversation.SubPartyConversationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class SubPartyConversationDaoImp
		extends BlinckRepositoryProvider<SubPartyConversation, Long>
		implements SubPartyConversationDao {

	public SubPartyConversationDaoImp(SubPartyConversationRepository repository) {
		super(repository);
	}

}