package net.henryco.blinckserver.mvc.model.repository.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface PartyConversationRepository extends JpaRepository<PartyConversation, Long> {

	List<PartyConversation> getAllByPartyOrderByMessagePart_DateDesc(Long party, Pageable pageable);
	long countAllByParty(Long party);
	void deleteAllByParty(Long party);
}