package net.henryco.blinckserver.mvc.model.repository.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface FriendshipConversationRepository extends JpaRepository<FriendshipConversation, Long> {


	List<FriendshipConversation> getAllByFriendshipOrderByMessagePart_Date_Desc(Long friendship, Pageable pageable);

	void removeAllByFriendship(Long friendship);

	long countAllByFriendship(Long friendship);
}