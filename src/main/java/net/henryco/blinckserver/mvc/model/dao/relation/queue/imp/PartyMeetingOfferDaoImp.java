package net.henryco.blinckserver.mvc.model.dao.relation.queue.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.PartyMeetingOfferDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.PartyMeetingOffer;
import net.henryco.blinckserver.mvc.model.repository.relation.queue.PartyMeetingOfferRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author Henry on 18/09/17.
 */
@Repository @Transactional
public class PartyMeetingOfferDaoImp
		extends BlinckRepositoryProvider<PartyMeetingOffer, Long>
		implements PartyMeetingOfferDao{


	public PartyMeetingOfferDaoImp(PartyMeetingOfferRepository repository) {
		super(repository);
	}

	private PartyMeetingOfferRepository getRepository() {
		return provideRepository();
	}


	@Override @Transactional
	public PartyMeetingOffer getById(Long id) {

		try {
			return super.getById(id);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	@Override @Transactional
	public List<PartyMeetingOffer> getMeetingOfferListByPartyId(Long partyId) {
		return getRepository().getAllByParty(partyId);
	}

	@Override @Transactional
	public List<PartyMeetingOffer> getAllWithUserAndPartyId(Long partyId, Long userId) {
		return getRepository().getAllByPartyAndUsersIsContaining(partyId, userId);
	}

	@Override @Transactional
	public void deleteAllByParty(Long party) {
		getRepository().deleteAllByParty(party);
	}
}
