package net.henryco.blinckserver.mvc.model.entity.relation.queue;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 18/09/17.
 */
@Data @Entity
@NoArgsConstructor
public class PartyMeetingOffer {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "party",
			nullable = false,
			updatable = false
	) Long party;


	private @ElementCollection(
			targetClass = Long.class
	) @Column(
			name = "users",
			nullable = false
	) Set<Long> users;


	private @Embedded @JoinColumn(
			name = "meeting",
			nullable = false
	) Meeting offer;


}