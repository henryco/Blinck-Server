package net.henryco.blinckserver.mvc.model.entity.relation.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

/**
 * <h1>Friendship conversation JSON:</h1>
 *	<h3>
 * 	[&nbsp;
 * 		{
 * 			"id": 			LONG, &nbsp;
 * 			"message": 		CHAR[512], &nbsp;
 * 			"timestamp": 	DATE/LONG, &nbsp;
 * 			"author": 		LONG, &nbsp;
 * 			"friendship":	LONG
 *		}
 *	&nbsp;]</h3>
 *	@author Henry on 28/08/17.
 */ @Entity
@Data @NoArgsConstructor
public class FriendshipConversation
		implements Cloneable, ConversationEntity {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) @JsonProperty(
			"id"
	) Long id;


	private @Embedded @JoinColumn(
			updatable = false,
			nullable = false,
			name = "message"
	) MessagePart messagePart;


	private @Column(
			name = "friendship_id",
			updatable = false,
			nullable = false
	) @JsonProperty(
			"friendship"
	) Long friendship;


	@Override
	public FriendshipConversation clone() {
		try {
			return (FriendshipConversation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long getTopic() {
		return getFriendship();
	}
}