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
				subParty = Helper.createNewSubParty(checked);
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
				party = Helper.createNewParty(subParty);
			}

			party.getSubParties().add(subParty.getId());
			if (party.getSubParties().size() == 2)
				party.getDetails().setInQueue(false);

			Party saved = partyDao.save(party);
			subParty.setParty(saved);
			subPartyDao.save(subParty);

			return saved;

		} catch (PersistenceException e) {
			e.printStackTrace();
			joinToExistingOrCreateParty(subParty);
		}
		return null;
	}


	@Transactional
	public SubPartyQueue createCustomSubParty(final Long userId, final Type type) {

	 	subPartyQueueDao.deleteAllByOwnerId(userId);
		SubPartyQueue queue = Helper.createNewSubPartyQueue(Type.typeChecker(type));
		queue.getUsers().add(userId);
		queue.setOwner(userId);
		return subPartyQueueDao.save(queue);
	}


	@Transactional
	public SubPartyQueue[] getCustomSubPartyList(final Long userId) {
	 	return subPartyQueueDao.getAllWithUser(userId).toArray(new SubPartyQueue[0]);
	}


	@Transactional
	public SubParty[] getSubPartyWaitList(final Long userId) {
		return subPartyDao.getAllWithUser(userId).stream().filter(subParty ->
				subParty.getDetails().getInQueue()
						|| subParty.getParty() != null && subParty.getParty().getDetails().getInQueue()
		).toArray(SubParty[]::new);
	}


	@Transactional
	public Long[] getSubPartyMembers(final Long subPartyId) {
		return subPartyDao.getById(subPartyId).getUsers().toArray(new Long[0]);
	}


	@Transactional
	public SubParty startCustomSubParty(final Long customSubPartyId) {

	 	SubPartyQueue queue = subPartyQueueDao.getById(customSubPartyId);
		subPartyQueueDao.deleteById(customSubPartyId);

		SubParty newSubParty = Helper.createNewSubParty(queue.getType());
		newSubParty.getUsers().addAll(queue.getUsers());
		if (newSubParty.getUsers().size() == queue.getType().getDimension())
			newSubParty.getDetails().setInQueue(false);

		return subPartyDao.save(newSubParty);
	}


	@Transactional
	public SubPartyQueue getCustomSubParty(final Long customSubPartyId) {
		return CopyWriter.copy(subPartyQueueDao.getById(customSubPartyId));
	}


	@Transactional
	public Boolean deleteCustomSubParty(final Long userId, final Long customSubPartyId) {
		SubPartyQueue byId = subPartyQueueDao.getById(customSubPartyId);
		if (byId.getOwner().equals(userId) || byId.getUsers().isEmpty()) {
			subPartyQueueDao.deleteById(customSubPartyId);
			return true;
		}
		return false;
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

		Party party = subParty.getParty();
		List<SubParty> subParties = processPartyInQueue(party, subParty);

		if (subParties == null)
			return false;

		for (SubParty sub: subParties) {
			sub.setParty(null);

			if (sub.getUsers().remove(userId)) {

				if (!sub.getUsers().isEmpty()) {
					sub.getDetails().setInQueue(true);
					subPartyDao.save(sub);
				}

				else subPartyDao.getById(sub.getId());
			}

			else joinToExistingOrCreateParty(sub);
		}

		return true;
	}


	@Transactional
	protected List<SubParty> processPartyInQueue(Party party, SubParty subParty) {

	 	if (party != null) {
			if (party.getMeeting() != null)
				return null;

			List<SubParty> list = new ArrayList<>();
			for (Long sub : party.getSubParties())
				list.add(subPartyDao.getById(sub));
			partyDao.deleteById(party.getId());
			return list;
		}
		return new ArrayList<SubParty>(){{add(subParty);}};
	}




	private static abstract class Helper {

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
			party.setMeeting(null);
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


	private static abstract class CopyWriter {

		private static
		SubPartyQueue copy(SubPartyQueue queue) {

			SubPartyQueue subPartyQueue = new SubPartyQueue();
			subPartyQueue.setId(queue.getId());
			subPartyQueue.setType(queue.getType().copy());
			subPartyQueue.setUsers(new ArrayList<>(queue.getUsers()));
			subPartyQueue.setOwner(queue.getOwner());

			return subPartyQueue;
		}

	}

}