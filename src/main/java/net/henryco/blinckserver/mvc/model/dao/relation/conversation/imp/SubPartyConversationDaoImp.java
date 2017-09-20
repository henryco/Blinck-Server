package net.henryco.blinckserver.mvc.model.dao.relation.conversation.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import net.henryco.blinckserver.mvc.model.repository.relation.conversation.SubPartyConversationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

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

	private SubPartyConversationRepository getRepository() {
		return provideRepository();
	}

	@Override
	public List<SubPartyConversation> getLastNBySubPartyId(Long id, int page, int size) {
		return getRepository().getAllBySubPartyOrderByMessagePart_DateDesc(id, new PageRequest(page, size));
	}

	@Override
	public long countBySubPartyId(Long id) {
		return getRepository().countAllBySubParty(id);
	}

	@Override
	public void deleteAllBySubPartyId(Long id) {
		getRepository().deleteAllBySubParty(id);
	}
}