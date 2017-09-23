package net.henryco.blinckserver.mvc.model.entity.relation.core.embeded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <h1>Match Type JSON:</h1>
 * <h2>
 *     {&nbsp;
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
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {

	public static final String MALE = "male";
	public static final String FEMALE = "female";
	public static final String BOTH = "both";

	public static final Integer DIMENSION_1x1 = 1;
	public static final Integer DIMENSION_2x2 = 2;
	public static final Integer DIMENSION_3x3 = 3;
	public static final Integer DIMENSION_5x5 = 5;
	public static final Integer DIMENSION_10x10 = 10;


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


	@JsonIgnore public Type invertedCopy() {
		return new Type(wanted, ident, dimension);
	}

	@JsonIgnore public Type copy() {
		return new Type(ident, wanted, dimension);
	}

	public static Type typeChecker(Type type) {

		if (type.getWanted().equalsIgnoreCase(BOTH) || type.getIdent().equalsIgnoreCase(BOTH))
			return newBoth(type.getDimension());

		if (type.getWanted().equalsIgnoreCase(MALE)) {
			if (type.getIdent().equalsIgnoreCase(FEMALE))
				return newFemMale(type.getDimension());
			return newMaleMale(type.getDimension());
		}

		if (type.getWanted().equalsIgnoreCase(FEMALE)) {
			if (type.getIdent().equalsIgnoreCase(MALE))
				return newMaleFem(type.getDimension());
			return newFemFem(type.getDimension());
		}

		return newBoth(type.getDimension());
	}


	public static Type newMaleFem(Integer dimension) {
		return new Type(MALE, FEMALE, dimension);
	}

	public static Type newFemMale(Integer dimension) {
		return new Type(FEMALE, MALE, dimension);
	}

	public static Type newMaleMale(Integer dimension) {
		return new Type(MALE, MALE, dimension);
	}

	public static Type newFemFem(Integer dimension) {
		return new Type(FEMALE, FEMALE, dimension);
	}

	public static Type newBoth(Integer dimension) {
		return new Type(BOTH, BOTH, dimension);
	}

}
