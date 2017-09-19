package net.henryco.blinckserver.mvc.model.entity.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 06/09/17.
 */
@Entity @Data @NoArgsConstructor
public class UpdateNotification {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "time_stamp",
			nullable = false,
			updatable = false
	) @Temporal(
			TIMESTAMP
	) Date date;


	private @Column(
			name = "target_user_id",
			nullable = false,
			updatable = false
	) Long targetUserId;


	private @OneToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			updatable = false,
			nullable = false,
			unique = true,
			name = "details"
	) Details details;




	@Entity @Data
	@NoArgsConstructor
	public static class Details {

		private @Id @Column(
				name = "id",
				unique = true
		) @GeneratedValue(
				strategy = AUTO
		) Long id;


		private @Column(
				name = "notification",
				nullable = false,
				updatable = false
		) String notification;


		private @Column(
				name = "type",
				nullable = false,
				updatable = false
		) String type;

	}


}