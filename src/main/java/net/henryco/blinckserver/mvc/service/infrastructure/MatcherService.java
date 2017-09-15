package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyQueueDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubPartyQueue;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class MatcherService {


	private final SubPartyQueueDao subPartyQueueDao;
	private final SubPartyDao subPartyDao;
	private final PartyDao partyDao;


	@Autowired
	public MatcherService(SubPartyQueueDao subPartyQueueDao,
						  SubPartyDao subPartyDao,
						  PartyDao partyDao) {
		this.subPartyQueueDao = subPartyQueueDao;
		this.subPartyDao = subPartyDao;
		this.partyDao = partyDao;
	}


	/**
	 * <h1>Synchronized method.</h1>
	 * <h2>TODO: 13/09/17 REWORK TO ASYNC FORM </h2>
	 * @return SubParty which user joined or was created.
	 * @see SubParty
	 */ @Transactional
	public synchronized SubParty jointToExistingOrCreateSubParty(final Long userId, final Type type) {

		try {

			final Type checked = Type.typeChecker(type);
			SubParty subParty = subPartyDao.getRandomFirstInQueue(checked.getWanted(), checked.getIdent(), checked.getDimension());
			if (subParty == null) {
				subParty = createNewSubParty(checked);
			}

			subParty.getUsers().add(userId);
			if (subParty.getUsers().size() == checked.getDimension())
				subParty.getDetails().setInQueue(false);

			return subPartyDao.save(subParty);

		} catch (PersistenceException e) {
			e.printStackTrace();
			jointToExistingOrCreateSubParty(userId, type);
		}
		return null;
	}


	/**
	 * <h1>Synchronized method.</h1>
	 * <h2>TODO: 13/09/17 REWORK TO ASYNC FORM </h2>
	 * @return Party which user joined or was created.
	 * @see Party
	 */ @Transactional
	public synchronized Party joinToExistingOrCreateParty(final SubParty subParty) {

		try {

			final Type type = Type.typeChecker(subParty.getDetails().getType());
			Party party = partyDao.getRandomFirstInQueue(type.getWanted(), type.getIdent(), type.getDimension());
			if (party == null) {
				party = createNewParty(subParty);
			}

			party.getSubParties().add(subParty);
			if (party.getSubParties().size() == 2)
				party.getDetails().setInQueue(false);

			return partyDao.save(party);

		} catch (PersistenceException e) {
			e.printStackTrace();
			joinToExistingOrCreateParty(subParty);
		}
		return null;
	}


	@Transactional
	public SubPartyQueue createCustomSubParty(final Long userId, final Type type) {

	 	subPartyQueueDao.deleteAllByOwnerId(userId);
		SubPartyQueue queue = createNewSubPartyQueue(Type.typeChecker(type));
		queue.getUsers().add(userId);
		queue.setOwner(userId);
		return subPartyQueueDao.save(queue);
	}


	@Transactional
	public Long[] getCustomSubPartyList(final Long userId) {

	 	return subPartyQueueDao.getAllWithUser(userId)
				.stream().map(SubPartyQueue::getId)
		.toArray(Long[]::new);
	}


	@Transactional
	public Long[] getSubPartyList(final Long userId) {
	 	return subPartyDao.getAllWithUserInQueue(userId)
				.stream().map(SubParty::getId)
		.toArray(Long[]::new);
	}


	@Transactional
	public SubParty startCustomSubParty(final Long customSubPartyId) {

	 	SubPartyQueue queue = subPartyQueueDao.getById(customSubPartyId);
		subPartyQueueDao.deleteById(customSubPartyId);

		SubParty newSubParty = createNewSubParty(queue.getType());
		newSubParty.getUsers().addAll(queue.getUsers());
		if (newSubParty.getUsers().size() == queue.getType().getDimension())
			newSubParty.getDetails().setInQueue(false);

		return subPartyDao.save(newSubParty);
	}


	@Transactional
	public SubPartyQueue getCustomSubParty(final Long customSubPartyId) {
		return subPartyQueueDao.getById(customSubPartyId);
	}


	@Transactional
	public void deleteCustomSubParty(final Long userId, final Long customSubPartyId) {
		SubPartyQueue byId = subPartyQueueDao.getById(customSubPartyId);
		if (byId.getOwner().equals(userId)) {
			subPartyQueueDao.deleteById(customSubPartyId);
		}
	}


	@Transactional
	public SubPartyQueue leaveCustomSubParty(final Long userId, final Long customSubPartyId) {

		SubPartyQueue byId = subPartyQueueDao.getById(customSubPartyId);
		byId.getUsers().remove(userId);
		return subPartyQueueDao.save(byId);
	}


	@Transactional
	public boolean addUserToCustomSubParty(final Long user, final Long customSubPartyId) {

	 	SubPartyQueue queue = subPartyQueueDao.getById(customSubPartyId);

		if (queue.getUsers().contains(user)) return false;
		if (queue.getUsers().size() == queue.getType().getDimension()) return false;

		queue.getUsers().add(user);
		subPartyQueueDao.save(queue);

		return true;
	}


	@Transactional
	public synchronized boolean leaveSearchQueue(final Long userId, final Long subPartyId) {

		SubParty subParty = subPartyDao.getById(subPartyId);
		if (!subParty.getUsers().contains(userId)) return false;

		List<SubParty> subParties = new ArrayList<SubParty>(){{add(subParty);}};
		Party party = subParty.getParty();

		if (party != null) {
			if (!party.getDetails().getInQueue())
				return false;

			partyDao.deleteById(party.getId());
			subParties = party.getSubParties();
		}

		for (SubParty sub: subParties) {
			sub.setParty(null);
			if (sub.getUsers().contains(userId)) {
				sub.getUsers().remove(userId);
				sub.getDetails().setInQueue(true);
				subPartyDao.save(sub);
			}
			else joinToExistingOrCreateParty(sub);
		}
		return true;
	}


	private static
	SubParty createNewSubParty(Type type) {

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
	SubPartyQueue createNewSubPartyQueue(Type type) {

		SubPartyQueue queue = new SubPartyQueue();
	 	queue.setType(type);
	 	queue.setUsers(new ArrayList<>());
	 	return queue;
	}


	private static
	Details createNewDetails(Type type) {

	 	Details details = new Details();
		details.setInQueue(true);
		details.setType(type);
		return details;
	}

}