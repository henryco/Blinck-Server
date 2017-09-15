package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class PartyService {

	private final PartyDao partyDao;

	@Autowired
	public PartyService(PartyDao partyDao) {
		this.partyDao = partyDao;
	}


	@Transactional
	public Party[] getAllParties() {
		return partyDao.getAll().toArray(new Party[0]);
	}

}