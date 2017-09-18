package net.henryco.blinckserver.mvc.model.repository.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface SubPartyConversationRepository extends JpaRepository<SubPartyConversation, Long> {

	List<SubPartyConversation> getAllBySubPartyOrderByMessagePart_DateDesc(Long subParty, Pageable pageable);
	long countAllBySubParty(Long subParty);
	void deleteAllBySubParty(Long subParty);
}