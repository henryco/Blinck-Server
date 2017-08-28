package net.henryco.blinckserver.mvc.model.repository.relations.dual;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface FriendshipConversationRepository extends JpaRepository<FriendshipConversation, Long> {

}