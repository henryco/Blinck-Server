package net.henryco.blinckserver.mvc.model.repository.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.PartyConversation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface PartyConversationRepository extends JpaRepository<PartyConversation, Long> {

}