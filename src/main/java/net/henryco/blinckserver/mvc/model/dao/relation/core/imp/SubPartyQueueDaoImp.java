package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyQueueDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubPartyQueue;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyQueueRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 15/09/17.
 */
@Repository @Transactional
public class SubPartyQueueDaoImp
		extends BlinckRepositoryProvider<SubPartyQueue, Long>
		implements SubPartyQueueDao {

	public SubPartyQueueDaoImp(SubPartyQueueRepository repository) {
		super(repository);
	}

	private SubPartyQueueRepository getRepository() {
		return provideRepository();
	}

	@Override @Transactional
	public void deleteAllByOwnerId(Long id) {
		getRepository().removeAllByOwner(id);
	}

	@Override @Transactional
	public List<SubPartyQueue> getAllWithUser(Long user) {
		return getRepository().getAllByUsersIsContainingOrOwner(user, user);
	}
}