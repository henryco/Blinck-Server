package net.henryco.blinckserver.mvc.service.relation.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class PartyService extends BlinckDaoProvider<Party, Long> {


	@Data @NoArgsConstructor
	public static final class PartyInfo
			implements Serializable {

		private @JsonProperty Long id;
		private @JsonProperty("enable_since") Date date;
		private @JsonProperty("sub_parties") List<Long> subParties;
		private @JsonProperty("users") List<Long> users;

		private PartyInfo(Party party, List<Long> users) {
			this.id = party.getId();
			this.date = party.getActivationTime();
			this.subParties = new ArrayList<>(party.getSubParties());
			this.users = new ArrayList<>(users);
		}
	}

	private final SubPartyDao subPartyDao;


	@Autowired
	public PartyService(PartyDao partyDao,
						SubPartyDao subPartyDao) {
		super(partyDao);
		this.subPartyDao = subPartyDao;
	}


	private PartyInfo createPartyInfo(Party party) {

		final List<Long> users = new ArrayList<>();
		for (Long sub : party.getSubParties())
			users.addAll(subPartyDao.getById(sub).getUsers());
		return new PartyInfo(party, users);
	}


	private PartyDao getDao() {
		return provideDao();
	}



	@Transactional
	public PartyInfo[] getAllPartyInfoWithUser(Long userId) {
		return subPartyDao.getAllWithUserInParty(userId)
				.stream().map(s -> createPartyInfo(s.getParty()))
		.toArray(PartyInfo[]::new);
	}


	@Transactional
	public PartyInfo getPartyInfo(Long partyId) {
		return createPartyInfo(getDao().getById(partyId));
	}



	@Transactional
	public Party[] getAllParties() {
		return getDao().getAll().toArray(new Party[0]);
	}

}