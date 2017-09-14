package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class MatcherService {


	private final SubPartyDao subPartyDao;
	private final PartyDao partyDao;

	@Autowired
	public MatcherService(SubPartyDao subPartyDao, PartyDao partyDao) {
		this.subPartyDao = subPartyDao;
		this.partyDao = partyDao;
	}


	/**
	 * <h1>Synchronized method.</h1>
	 * <h2>TODO: 13/09/17 REWORK TO ASYNC FORM </h2>
	 * @return SubParty which user joined or was created.
	 * @see SubParty
	 */ @Transactional
	public synchronized SubParty jointToExistingOrCreateSubParty(Long userId, Details.Type type) {

		SubParty subParty = subPartyDao.getRandomFirstInQueue(type.getWanted(), type.getIdent(), type.getDimension());
		if (subParty == null) {
			subParty = createNewSubParty(type);
		}

		subParty.getUsers().add(userId);
		if (subParty.getUsers().size() == type.getDimension())
			subParty.getDetails().setInQueue(false);

		return subPartyDao.save(subParty);
	}


	/**
	 * <h1>Synchronized method.</h1>
	 * <h2>TODO: 13/09/17 REWORK TO ASYNC FORM </h2>
	 * @return Party which user joined or was created.
	 * @see Party
	 */ @Transactional
	public synchronized Party joinToExistingOrCreateParty(SubParty subParty) {

	 	Details.Type type = subParty.getDetails().getType();
		Party party = partyDao.getRandomFirstInQueue(type.getWanted(), type.getIdent(), type.getDimension());
		if (party == null) {
			party = createNewParty(subParty);
		}

		party.getSubParties().add(subParty);
		if (party.getSubParties().size() == 2)
			party.getDetails().setInQueue(false);

		return partyDao.save(party);
	}




	private static
	SubParty createNewSubParty(Details.Type type) {

		SubParty subParty = new SubParty();
		subParty.setUsers(new ArrayList<>());
		subParty.setDetails(createNewDetails(type));
		return subParty;
	}

	private static
	Party createNewParty(SubParty subParty) {

	 	Party party = new Party();
		party.setDetails(createNewDetails(subParty.getDetails().getType().invertedCopy()));
		party.setSubParties(new ArrayList<>());
	 	return party;
	}

	private static
	Details createNewDetails(Details.Type type) {

	 	Details details = new Details();
		details.setInQueue(true);
		details.setType(type);
		return details;
	}


}