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


	/**
	 * <h1>Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "id":		LONG, 			&nbsp;
	 *         "ident":		CHAR[255], 		&nbsp;
	 *         "wanted":	CHAR[255]
	 *     &nbsp;}
	 * </h2>
	 * <h3>Types: <br><br>
	 *     &nbsp;MALE:		&nbsp;"male" 	<br>
	 *     &nbsp;FEMALE:	&nbsp;"female"	<br>
	 *     &nbsp;BOTH:		&nbsp;"both"
	 * </h3>
	 *
	 */
	@Entity @Data
	@NoArgsConstructor
	public static final class Type {

		public static final String MALE = "male";
		public static final String FEMALE = "female";
		public static final String BOTH = "both";


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



		public static Type typeAdapter(Type type) {

			if (type.getWanted().equals(BOTH) || type.getIdent().equals(BOTH))
				return newBoth();

			if (type.getWanted().equals(MALE)) {
				if (type.getIdent().equals(FEMALE))
					return newFemMale();
				return newMaleMale();
			}

			if (type.getWanted().equals(FEMALE)) {
				if (type.getIdent().equals(MALE))
					return newMaleMale();
				return newFemFem();
			}

			return newBoth();
		}


		public static Type newMaleFem() {
			Type type = new Type();
			type.setIdent(MALE);
			type.setWanted(FEMALE);
			return type;
		}

		public static Type newFemMale() {
			Type type = new Type();
			type.setIdent(FEMALE);
			type.setWanted(MALE);
			return type;
		}

		public static Type newMaleMale() {
			Type type = new Type();
			type.setIdent(MALE);
			type.setWanted(MALE);
			return type;
		}

		public static Type newFemFem() {
			Type type = new Type();
			type.setIdent(FEMALE);
			type.setWanted(FEMALE);
			return type;
		}

		public static Type newBoth() {
			Type type = new Type();
			type.setIdent(BOTH);
			type.setWanted(BOTH);
			return type;
		}

	}


}