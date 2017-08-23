package net.henryco.blinckserver.mvc.model.entity.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Henry on 23/08/17.
 */
@Entity @Data
@NoArgsConstructor
@Table(name = "USER_PROFILE")
public class UserBaseProfile {



	private @Id @Column(
			unique = true,
			name = "user_id"
	) long id;


	private @Column(
			name = "birthday"
	) String birthday;


	private @ManyToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			table = "USER_PROFILE_NAME",
			name = "name_id",
			unique = true
	) UserNameEntity userName;



}
