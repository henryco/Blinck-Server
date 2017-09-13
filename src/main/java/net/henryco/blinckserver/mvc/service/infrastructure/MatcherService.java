package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class MatcherService {


	private final SubPartyDao subPartyDao;

	@Autowired
	public MatcherService(SubPartyDao subPartyDao) {
		this.subPartyDao = subPartyDao;
	}


	@Transactional
	public void jointToExistingOrCreateSubParty() {


	}

}