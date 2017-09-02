package net.henryco.blinckserver.mvc.model.entity.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.core.UserCoreProfile;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class ReportList {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) long id;


	private @Column(
			name = "reporter_id",
			updatable = false,
			nullable = false
	) long reporterId;


	private @Column(
			name = "reported_id",
			updatable = false,
			nullable = false
	) long reportedId;


	private @Column(
			name = "reason",
			updatable = false
	) String reason;


}