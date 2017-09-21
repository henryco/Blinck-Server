package net.henryco.blinckserver.mvc.service.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class SubPartyService
		extends BlinckDaoProvider<SubParty, Long> {

	private static final long TIME_BEFORE_DELETE = 86_400_000L; // 1 Day

	@Data @NoArgsConstructor
	public static final class SubPartyInfo
			implements Serializable{

		private Long id;
		private Long party;
		private Type type;
		private List<Long> users;

		private SubPartyInfo(SubParty subParty) {

			this.id = subParty.getId();
			this.party = subParty.getParty() == null ? null : subParty.getParty().getId();
			this.type = subParty.getDetails().getType();
			this.users = new ArrayList<>(subParty.getUsers());
		}
	}

	private final PartyDao partyDao;

	@Autowired
	public SubPartyService(SubPartyDao subPartyDao,
						   PartyDao partyDao) {
		super(((subPartyDao)));
		this.partyDao = partyDao;
	}

	private SubPartyDao getDao() {
		return provideDao();
	}


	@Transactional
	public SubParty[] getAllSubParties() {
		return getDao().getAll().toArray(new SubParty[0]);
	}


	@Transactional
	public SubParty[] getAllSubPartiesWithUserInParty(Long userId) {

		List<SubParty> all = getDao().getAllWithUserInParty(userId);
		List<SubParty> newList = new ArrayList<>();

		for (SubParty subParty: all) {

			Party party = subParty.getParty();
			Meeting meeting = party.getMeeting();

			if (meeting == null) {
				newList.add(subParty);
				continue;
			}

			Date deleteTime = new Date(meeting.getTime().getTime() + TIME_BEFORE_DELETE);
			if (deleteTime.before(new Date(System.currentTimeMillis()))) {
				for (Long subId : party.getSubParties())
					getDao().deleteById(subId);
				partyDao.deleteById(party.getId());
			} else newList.add(subParty);
		}

		return newList.toArray(new SubParty[0]);
	}


	@Transactional
	public Long[] getSubPartiesIdListWithUserInParty(Long userId) {

		return Arrays.stream(getAllSubPartiesWithUserInParty(userId))
				.map(SubParty::getId).toArray(Long[]::new);
	}


	@Transactional
	public SubPartyInfo getSubPartyInfoByIdAndUser(Long id, Long userId) {

		SubParty subParty = getDao().getById(id);
		if (subParty.getUsers().contains(userId))
			return new SubPartyInfo(subParty);
		return null;
	}


	@Transactional
	public SubPartyInfo[] getSubPartyInfoListWithUserInParty(Long userId) {

		return Arrays.stream(getAllSubPartiesWithUserInParty(userId))
				.map(SubPartyInfo::new).toArray(SubPartyInfo[]::new);
	}


	@Transactional
	public SubPartyInfo[] getRelatedSubParties(Long subPartyId) {

		SubParty subParty = getDao().getById(subPartyId);
		Party party = subParty.getParty();

		if (party == null || party.getSubParties() == null) {
			return new SubPartyInfo[]{new SubPartyInfo(subParty)};
		}

		List<SubPartyInfo> infoList = new ArrayList<>();
		for (Long sub : party.getSubParties())
			infoList.add(new SubPartyInfo(getDao().getById(sub)));

		return infoList.toArray(new SubPartyInfo[0]);
	}


	@Transactional
	public boolean isExistsWithUser(Long id, Long userId) {
		return getDao().existsWithUser(id, userId);
	}


	@Transactional
	public Long[] getSubPartyUsers(Long subPartyId) {
		return getDao().getById(subPartyId).getUsers().toArray(new Long[0]);
	}

}