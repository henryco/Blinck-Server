package net.henryco.blinckserver.mvc.model.entity.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;

import javax.persistence.*;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class ReportList {

	private @Id @Column(
			unique = true
	) long id;


	private @ManyToOne(
			cascade = CascadeType.ALL,
			optional = false,
			targetEntity = UserBaseProfile.class
	) @JoinColumn(
			name = "reporter_id"
	) long reporterId;


	private @ManyToOne(
			cascade = CascadeType.ALL,
			optional = false,
			targetEntity = UserBaseProfile.class
	) @JoinColumn(
			name = "reported_id"
	) long reportedId;


	private @Column(
			name = "reason"
	) String reason;
}