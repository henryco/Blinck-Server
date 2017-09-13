package net.henryco.blinckserver.mvc.service.relation.core;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}