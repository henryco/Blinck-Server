package net.henryco.blinckserver.mvc.model.entity.profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;

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


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "name_id",
			unique = true
	) UserNameEntity userName;


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "auth_id",
			unique = true
	) UserAuthProfile authProfile;


}