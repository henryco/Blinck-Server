package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.core.UserCoreProfile;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

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
			name = "friendship_type"
	) Integer friendshipType;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "user_id_1"
	)
	UserCoreProfile user1;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "user_id_2"
	)
	UserCoreProfile user2;


}