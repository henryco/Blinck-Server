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
public class BlackList {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = GenerationType.AUTO
	) long id;


	private @ManyToOne(
			cascade = CascadeType.ALL,
			optional = false,
			targetEntity = UserBaseProfile.class
	) @JoinColumn(
			name = "block_owner_id"
	) long userId;


	private @ManyToOne(
			cascade = CascadeType.ALL,
			optional = false,
			targetEntity = UserBaseProfile.class
	) @JoinColumn(
			name = "blocked_id"
	) long blockedUserId;


}