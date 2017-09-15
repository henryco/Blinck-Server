package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 15/09/17.
 */
@Entity @Data
@NoArgsConstructor
public class SubPartyQueue implements Cloneable {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "owner",
			nullable = false,
			updatable = false
	) Long owner;


	private @Embedded @JoinColumn(
			updatable = false,
			nullable = false,
			name = "typo"
	) Type type;


	private @ElementCollection(
			targetClass = Long.class
	) @Column(
			name = "users",
			nullable = false
	) List<Long> users;


	@Override
	public SubPartyQueue clone() {
		try {
			// Not deep copy, but enough for us
			return (SubPartyQueue) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}