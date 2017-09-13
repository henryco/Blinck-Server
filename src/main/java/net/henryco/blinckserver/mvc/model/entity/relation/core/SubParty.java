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
			nullable = false,
			unique = true,
			name = "details"
	) Details details;


	private @ElementCollection(
			targetClass = Long.class
	) @Column(
			name = "users",
			nullable = false
	) List<Long> users;


	@Entity @Data
	@NoArgsConstructor
	public static final class Details {

		private @Id @Column(
				name = "id",
				unique = true
		) @GeneratedValue(
				strategy = AUTO
		) Long id;


		private @OneToOne(
				cascade = ALL,
				optional = false
		) @JoinColumn(
				updatable = false,
				nullable = false,
				name = "typo"
		) Type type;


		private @Column(
				name = "in_queue",
				nullable = false
		) Boolean inQueue;

	}


	/**
	 * <h1>Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "id":		LONG, 			&nbsp;
	 *         "ident":		CHAR[255], 		&nbsp;
	 *         "wanted":	CHAR[255], 		&nbsp;
	 *         "dimension":	INTEGER
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

		public static final Integer DIMENSION_1x1 = 1;
		public static final Integer DIMENSION_2x2 = 2;
		public static final Integer DIMENSION_3x3 = 3;
		public static final Integer DIMENSION_5x5 = 5;
		public static final Integer DIMENSION_10x10 = 10;

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


		private @Column(
				name = "dimension",
				updatable = false,
				nullable = false
		) Integer dimension;



		public static Type typeAdapter(Type type) {

			if (type.getWanted().equals(BOTH) || type.getIdent().equals(BOTH))
				return newBoth(type.getDimension());

			if (type.getWanted().equals(MALE)) {
				if (type.getIdent().equals(FEMALE))
					return newFemMale(type.getDimension());
				return newMaleMale(type.getDimension());
			}

			if (type.getWanted().equals(FEMALE)) {
				if (type.getIdent().equals(MALE))
					return newMaleMale(type.getDimension());
				return newFemFem(type.getDimension());
			}

			return newBoth(type.getDimension());
		}


		public static Type newOne(String ident, String wanted, Integer dim) {
			Type type = new Type();
			type.setIdent(ident);
			type.setWanted(wanted);
			type.setDimension(dim);
			return type;
		}

		public static Type newMaleFem(Integer dimension) {
			return newOne(MALE, FEMALE, dimension);
		}

		public static Type newFemMale(Integer dimension) {
			return newOne(FEMALE, MALE, dimension);
		}

		public static Type newMaleMale(Integer dimension) {
			return newOne(MALE, MALE, dimension);
		}

		public static Type newFemFem(Integer dimension) {
			return newOne(FEMALE, FEMALE, dimension);
		}

		public static Type newBoth(Integer dimension) {
			return newOne(BOTH, BOTH, dimension);
		}

	}


}