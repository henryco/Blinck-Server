package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class Party {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Embedded @JoinColumn(
			name = "meeting"
	) Meeting meeting;


	private @ElementCollection(
			targetClass = Long.class
	) @Column(
			name = "sub_parties",
			nullable = false
	) List<Long> subParties;


	private @Embedded @JoinColumn(
			nullable = false,
			unique = true,
			name = "search_details"
	) Details details;


}