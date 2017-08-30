package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.repository.relation.core.PartyRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class PartyDaoImp
		extends BlinckRepositoryProvider<Party, Long>
		implements PartyDao {

	public PartyDaoImp(PartyRepository repository) {
		super(repository);
	}

}