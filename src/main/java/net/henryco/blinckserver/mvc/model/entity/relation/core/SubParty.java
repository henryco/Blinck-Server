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
public class SubParty {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "sub_party_id",
			nullable = false,
			updatable = false
	) Long subPartyId;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "party_id",
			updatable = false
	) Party party;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "user_id",
			updatable = false,
			unique = true
	) UserCoreProfile user;

	// TODO: 02/09/17 TESTS, maybe remove hard reference

}