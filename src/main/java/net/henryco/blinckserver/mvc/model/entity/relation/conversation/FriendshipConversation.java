package net.henryco.blinckserver.mvc.model.entity.relation.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

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
		implements Cloneable {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) @JsonProperty(
			"id"
	) Long id;


	private @Column(
			name = "message",
			length = 512
	) @JsonProperty(
			"message"
	) String message;

	// TODO: 20/09/17 REPLACE WITH MESSAGE PART
	private @Column(
			name = "time_stamp",
			updatable = false,
			nullable = false
	) @Temporal(
			TIMESTAMP
	) @JsonProperty(
			"timestamp"
	) Date date;


	private @Column(
			name = "author_id",
			updatable = false,
			nullable = false
	) @JsonProperty(
			"author"
	) Long author;


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

}