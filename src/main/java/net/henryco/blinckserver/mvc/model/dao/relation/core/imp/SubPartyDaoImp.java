package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class SubPartyDaoImp
		extends BlinckRepositoryProvider<SubParty, Long>
		implements SubPartyDao {

	public SubPartyDaoImp(SubPartyRepository repository) {
		super(repository, false);
	}

}