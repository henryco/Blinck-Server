package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 31/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class AdminVerificationQueue {


	private @Id @Column(
			unique = true
	) String id;


	private @Column(
			name = "registration_time"
	) @Temporal(
			TIMESTAMP
	) Date registrationTime;


}