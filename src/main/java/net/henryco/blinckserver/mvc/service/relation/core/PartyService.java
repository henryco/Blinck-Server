package net.henryco.blinckserver.mvc.service.relation.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.henryco.blinckserver.mvc.service.relation.core.PartyService.Helper.activateMeeting;
import static net.henryco.blinckserver.mvc.service.relation.core.PartyService.Helper.compareActivationTime;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class PartyService extends BlinckDaoProvider<Party, Long> {


	public static final Long TIME_TO_ACTIVATE = 7_200_000L; // 2h

	@Data @NoArgsConstructor
	public static final class PartyInfo
			implements Serializable {

		private @JsonProperty("id") Long id;
		private @JsonProperty("meeting") Meeting meeting;
		private @JsonProperty("sub_parties") List<Long> subParties;
		private @JsonProperty("users") List<Long> users;

		private PartyInfo(Party party, List<Long> subParties, List<Long> users) {
			this.id = party.getId();
			this.meeting = party.getMeeting();
			this.subParties = subParties;
			this.users = users;
		}

		public final boolean isActiveAndContainsUser(Long userId) {
			return getUsers().contains(userId) &&
					getMeeting() != null &&
					getMeeting().getActivationTime()
							.before(new Date(System.currentTimeMillis()));
		}
	}

	private final SubPartyDao subPartyDao;


	@Autowired
	public PartyService(PartyDao partyDao,
						SubPartyDao subPartyDao) {
		super(partyDao);
		this.subPartyDao = subPartyDao;
	}


	protected static abstract class Helper {

		protected static
		boolean compareActivationTime(Party party) {
			return party.getMeeting() != null &&
					party.getMeeting().getActivationTime()
							.before(new Date(System.currentTimeMillis()));
		}

		protected static
		Meeting activateMeeting(Meeting meeting) {

			Date time = meeting.getTime();
			Date active = new Date(time.getTime() - TIME_TO_ACTIVATE);
			return new Meeting(time, active, meeting.getVenue());
		}

	}



	private PartyInfo createPartyInfo(Party party, Long userId) {

		final List<Long> subParties = new ArrayList<>();
		final List<Long> users = new ArrayList<>();

		for (Long sub : party.getSubParties()) {

			List<Long> list = subPartyDao.getById(sub).getUsers();
			if (list.contains(userId) || compareActivationTime(party)) {
				subParties.add(sub);
				users.addAll(list);
			}
		}

		return new PartyInfo(party, subParties, users);
	}


	private PartyDao getDao() {
		return provideDao();
	}


	@Transactional
	public Long[] getAllUsersInParty(Long partyId) {

		final List<Long> users = new ArrayList<>();
		for (Long sub : getDao().getById(partyId).getSubParties())
			users.addAll(subPartyDao.getById(sub).getUsers());
		return users.toArray(new Long[0]);
	}


	@Transactional
	public PartyInfo[] getAllPartyInfoWithUser(Long userId) {
		return subPartyDao.getAllWithUserInParty(userId)
				.stream().map(s -> createPartyInfo(s.getParty(), userId))
		.toArray(PartyInfo[]::new);
	}


	@Transactional
	public Long[] getAllPartyInfoIdWithUser(Long userId) {
		return subPartyDao.getAllWithUserInParty(userId)
				.stream().map(SubParty::getId)
		.toArray(Long[]::new);
	}

	@Transactional
	public PartyInfo getPartyInfo(Long partyId, Long userId) {
		return createPartyInfo(getDao().getById(partyId), userId);
	}


	@Transactional
	public Party[] getAllParties() {
		return getDao().getAll().toArray(new Party[0]);
	}


	@Transactional
	public Boolean isPartyActive(Long partyId) {
		return getDao().isPartyActive(partyId);
	}

	@Transactional
	public Boolean setMeeting(Long partyId, Meeting meeting) {

		Party party = getDao().getById(partyId);
		party.setMeeting(activateMeeting(meeting));
		return getDao().save(party) != null;
	}

	@Transactional
	public boolean isPartyActiveAndContainsUser(Long partyId, Long userId) {
		return getPartyInfo(partyId, userId).isActiveAndContainsUser(userId);
	}


}