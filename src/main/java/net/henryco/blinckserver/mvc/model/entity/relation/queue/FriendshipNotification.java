package net.henryco.blinckserver.mvc.model.entity.relation.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * <h1>Friendship notification response JSON:</h1>
 *	<h2>
 * 	[&nbsp;
 * 		{
 * 			"notification": 	LONG, &nbsp;
 * 			"from": 			LONG, &nbsp;
 * 			"to": 				LONG, &nbsp;
 * 			"timestamp": 		DATE/LONG
 *		}
 *	&nbsp;]</h2>
 *	@author Henry on 04/09/17.
 */ @Entity
@Data @NoArgsConstructor
public class FriendshipNotification {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) @JsonProperty(
			value = "notification"
	) Long id;


	private @Column(
			name = "initiator",
			nullable = false,
			updatable = false
	) @JsonProperty(
			value = "from"
	) Long initiatorId;


	private @Column(
			name = "receiver",
			nullable = false,
			updatable = false
	) @JsonProperty(
			value = "to"
	) Long receiverId;


	private @Column(
			name = "time_stamp",
			nullable = false,
			updatable = false
	) @Temporal(
			TIMESTAMP
	) @JsonProperty(
			value = "timestamp"
	) Date date;


}