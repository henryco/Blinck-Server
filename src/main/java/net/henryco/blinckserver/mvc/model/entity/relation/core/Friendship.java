package net.henryco.blinckserver.mvc.model.entity.relation.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * <h1>Friendship response JSON:</h1>
 *	<h2>
 * 	[&nbsp;
 * 		{
 * 			"friendship": 	LONG, &nbsp;
 * 			"timestamp": 	DATE/LONG, &nbsp;
 * 			"user_1": 		LONG, &nbsp;
 * 			"user_2": 		LONG
 *		}
 *	&nbsp;]</h2>
 *	@author Henry on 28/08/17.
 *
 */ @Entity
@Data @NoArgsConstructor
public class Friendship
		implements Cloneable {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) @JsonProperty(
			value = "friendship"
	) Long id;


	private @Column(
			name = "relation_created",
			updatable = false,
			nullable = false
	) @Temporal(
			TIMESTAMP
	) @JsonProperty(
			value = "timestamp"
	) Date date;


	private @Column(
			name = "user_id_1",
			updatable = false,
			nullable = false
	) @JsonProperty(
			value = "user_1"
	) Long user1;


	private @Column(
			name = "user_id_2",
			updatable = false,
			nullable = false
	) @JsonProperty(
			value = "user_2"
	) Long user2;


	@Override
	public Friendship clone() {
		try {
			return (Friendship) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}