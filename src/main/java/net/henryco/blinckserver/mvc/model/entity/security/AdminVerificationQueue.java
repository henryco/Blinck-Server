package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 31/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class AdminVerificationQueue {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "registration_time"
	) @Temporal(
			TIMESTAMP
	) Date registrationTime;


	private @OneToOne(
			cascade = ALL,
			optional = false,
			targetEntity = AdminAuthProfile.class
	) @JoinColumn(
			name = "profile_id",
			unique = true
	) String adminProfile;


}