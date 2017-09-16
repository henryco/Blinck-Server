package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Henry on 13/09/17.
 */
@Service
public class SubPartyService {

	private final SubPartyDao subPartyDao;

	@Autowired
	public SubPartyService(SubPartyDao subPartyDao) {
		this.subPartyDao = subPartyDao;
	}

	@Transactional
	public SubParty[] getAllSubParties() {
		return subPartyDao.getAll().toArray(new SubParty[0]);
	}

	@Transactional
	public SubParty[] getAllSubPartiesWithUserInParty(Long userId) {
		return subPartyDao.getAllWithUserInParty(userId).toArray(new SubParty[0]);
	}
}