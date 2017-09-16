package net.henryco.blinckserver.mvc.model.entity.relation.core.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Henry on 14/09/17.
 */
@Embeddable @Data
@NoArgsConstructor
@AllArgsConstructor
public class Details {

	private @Embedded @JoinColumn(
			updatable = false,
			nullable = false,
			name = "typo"
	) Type type;


	private @Column(
			name = "in_queue",
			nullable = false
	) Boolean inQueue;

}
