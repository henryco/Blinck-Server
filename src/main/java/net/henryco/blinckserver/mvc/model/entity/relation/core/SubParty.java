package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public final class SubParty {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @ManyToOne(
			cascade = ALL
	) @JoinColumn(
			name = "party_id"
	) Party party;


	private @OneToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			updatable = false,
			nullable = false,
			unique = true,
			name = "typo"
	) Type type;


	private @ElementCollection(
			targetClass = Long.class
	) @Column(
			name = "users",
			nullable = false
	) List<Long> users;



	@Entity @Data
	@NoArgsConstructor
	public static final class Type {

		private @Id @Column(
				name = "id",
				unique = true
		) @GeneratedValue(
				strategy = AUTO
		) Long id;


		private @Column(
				name = "ident",
				nullable = false,
				updatable = false
		) String ident;


		private @Column(
				name = "wanted",
				nullable = false,
				updatable = false
		) String wanted;

	}


}