package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.core.UserCoreProfile;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class Friendship {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "relation_created",
			updatable = false,
			nullable = false
	) @Temporal(
			TIMESTAMP
	) Date date;


	private @Column(
			name = "user_id_1",
			updatable = false,
			nullable = false
	) Long user1;


	private @Column(
			name = "user_id_2",
			updatable = false,
			nullable = false
	) Long user2;


}