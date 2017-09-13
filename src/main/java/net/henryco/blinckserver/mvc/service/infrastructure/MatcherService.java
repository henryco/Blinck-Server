package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

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


	/**
	 * <h1>Synchronized method.</h1>
	 * <h2>TODO: 13/09/17 REWORK TO ASYNC FORM </h2>
	 * @return SubParty which user joined or was created.
	 * @see SubParty
	 */
	@Transactional
	public synchronized SubParty jointToExistingOrCreateSubParty(Long userId, SubParty.Type type) {

		SubParty subParty = subPartyDao.getFirstInQueue(type.getIdent(), type.getWanted(), type.getDimension());
		if (subParty == null) {
			subParty = createNewOne(type);
		}

		subParty.getUsers().add(userId);
		if (subParty.getUsers().size() == type.getDimension())
			subParty.getDetails().setInQueue(false);

		return subPartyDao.save(subParty);
	}



	private static SubParty createNewOne(SubParty.Type type) {

		SubParty.Details details = new SubParty.Details();
		details.setInQueue(true);
		details.setType(type);

		SubParty subParty = new SubParty();
		subParty.setUsers(new LinkedList<>());
		subParty.setDetails(details);

		return subParty;
	}

}