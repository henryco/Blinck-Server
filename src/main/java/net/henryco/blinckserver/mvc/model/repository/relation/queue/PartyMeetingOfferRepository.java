package net.henryco.blinckserver.mvc.model.repository.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.PartyMeetingOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 18/09/17.
 */
public interface PartyMeetingOfferRepository extends JpaRepository<PartyMeetingOffer, Long> {

	List<PartyMeetingOffer> getAllByParty(Long party);
}