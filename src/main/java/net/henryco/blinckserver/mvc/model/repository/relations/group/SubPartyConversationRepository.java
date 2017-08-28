package net.henryco.blinckserver.mvc.model.repository.relations.group;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface SubPartyConversationRepository extends JpaRepository<SubPartyConversation, Long> {

}