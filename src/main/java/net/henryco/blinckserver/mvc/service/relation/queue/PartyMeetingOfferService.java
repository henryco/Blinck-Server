package net.henryco.blinckserver.mvc.service.relation.queue;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.PartyMeetingOfferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class PartyMeetingOfferService {

	private final PartyMeetingOfferDao partyMeetingOfferDao;

	@Autowired
	public PartyMeetingOfferService(PartyMeetingOfferDao partyMeetingOfferDao) {
		this.partyMeetingOfferDao = partyMeetingOfferDao;
	}


}