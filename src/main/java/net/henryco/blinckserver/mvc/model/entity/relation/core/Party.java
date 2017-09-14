package net.henryco.blinckserver.mvc.model.entity.relation.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

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


	private @Column(
			name = "activation_time"
	) @Temporal(
			TIMESTAMP
	) Date activationTime;


	private @OneToOne(
			cascade = ALL,
			optional = false,
			targetEntity = SubParty.class
	) @JoinColumn(
			name = "sub_parties",
			unique = true
	) List<SubParty> subParties;


	private @Embedded @JoinColumn(
			nullable = false,
			unique = true,
			name = "details"
	) Details details;


}