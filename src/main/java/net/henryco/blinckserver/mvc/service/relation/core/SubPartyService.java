package net.henryco.blinckserver.mvc.service.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class SubPartyService
		extends BlinckDaoProvider<SubParty, Long> {


	@Data @NoArgsConstructor
	public static final class SubPartyInfo
			implements Serializable{

		private Long id;
		private Long party;
		private Type type;
		private List<Long> users;

		private SubPartyInfo(SubParty subParty) {

			this.id = subParty.getId();
			this.party = subParty.getParty().getId();
			this.type = subParty.getDetails().getType();
			this.users = new ArrayList<>(subParty.getUsers());
		}
	}


	@Autowired
	public SubPartyService(SubPartyDao subPartyDao) {
		super(((subPartyDao)));
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
		return getDao().getAllWithUserInParty(userId).toArray(new SubParty[0]);
	}


	@Transactional
	public Long[] getSubPartiesIdListWithUserInParty(Long userId) {

		return getDao().getAllWithUserInParty(userId)
				.stream().map(SubParty::getId)
		.toArray(Long[]::new);
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

		return getDao().getAllWithUserInParty(userId)
				.stream().map(SubPartyInfo::new)
		.toArray(SubPartyInfo[]::new);
	}

}