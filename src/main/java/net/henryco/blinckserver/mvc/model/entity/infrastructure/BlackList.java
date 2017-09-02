package net.henryco.blinckserver.mvc.model.entity.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class BlackList {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) long id;


	private @Column(
			name = "block_owner_id",
			updatable = false,
			nullable = false
	) long userId;


	private @Column(
			name = "blocked_id",
			updatable = false,
			nullable = false
	) long blockedUserId;


}