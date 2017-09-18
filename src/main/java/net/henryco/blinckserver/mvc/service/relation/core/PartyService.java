package net.henryco.blinckserver.mvc.service.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author Henry on 13/09/17.
 */
@Service
public class PartyService extends BlinckDaoProvider<Party, Long> {


	@Data @NoArgsConstructor
	public static final class PartyInfo
			implements Serializable {

		private PartyInfo(Party party) {
			// TODO: 17/09/17
		}
	}


	@Autowired
	public PartyService(PartyDao partyDao) {
		super(partyDao);
	}

	private PartyDao getDao() {
		return provideDao();
	}





	@Transactional
	public Party[] getAllParties() {
		return getDao().getAll().toArray(new Party[0]);
	}

}