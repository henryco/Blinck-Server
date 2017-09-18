package net.henryco.blinckserver.mvc.service.relation.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
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
		private @JsonProperty Type type;
		private @JsonProperty("enable_since") Date date;
		private @JsonProperty("sub_parties") List<Long> subParties;

		private PartyInfo(Party party) {
			this.id = party.getId();
			this.type = party.getDetails().getType();
			this.date = party.getActivationTime();
			this.subParties = new ArrayList<>(party.getSubParties());
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