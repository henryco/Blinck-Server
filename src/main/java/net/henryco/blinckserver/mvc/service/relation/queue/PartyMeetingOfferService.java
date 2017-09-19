package net.henryco.blinckserver.mvc.service.relation.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.queue.PartyMeetingOfferDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.PartyMeetingOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class PartyMeetingOfferService {


	@Data
	@NoArgsConstructor @AllArgsConstructor
	public static final class OfferInfo
			implements Serializable {

		private Long id;
		private Integer votes;
		private Meeting offer;

		private OfferInfo(PartyMeetingOffer o) {
			this(o.getId(), o.getUsers().size(), o.getOffer());
		}
	}

	private final PartyMeetingOfferDao offerDao;

	@Autowired
	public PartyMeetingOfferService(PartyMeetingOfferDao offerDao) {
		this.offerDao = offerDao;
	}


	@Transactional
	public OfferInfo[] getOfferList(Long partyId) {

		return offerDao.getMeetingOfferListByPartyId(partyId)
				.stream().map(OfferInfo::new)
		.toArray(OfferInfo[]::new);
	}


	@Transactional
	public Boolean addProposition(Long userId, Long partyId, Meeting proposition) {

		PartyMeetingOffer offer = new PartyMeetingOffer();
		offer.setParty(partyId);
		offer.setOffer(proposition);
		offer.setUsers(new HashSet<Long>(){{add(userId);}});

		return offerDao.save(offer) != null;
	}


}