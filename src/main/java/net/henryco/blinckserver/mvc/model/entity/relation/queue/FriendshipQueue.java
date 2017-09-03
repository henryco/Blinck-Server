package net.henryco.blinckserver.mvc.model.entity.relation.queue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 04/09/17.
 */
@Entity @Data
@NoArgsConstructor
public class FriendshipQueue {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "initiator",
			nullable = false,
			updatable = false
	) Long initiatorId;


	private @Column(
			name = "receiver",
			nullable = false,
			updatable = false
	) Long receiverId;


	private @Column(
			name = "time_stamp",
			nullable = false,
			updatable = false
	) @Temporal(
			TIMESTAMP
	) Date date;


}