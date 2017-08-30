package net.henryco.blinckserver.mvc.model.entity.profile.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.priv.UserPrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.pub.UserPublicProfile;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @author Henry on 23/08/17.
 */
@Entity @Data
@NoArgsConstructor @Proxy
@Table(name = "USER_PROFILE")
public class UserCoreProfile {


	private @Id @Column(
			unique = true,
			name = "user_id"
	) long id;


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "auth_id",
			unique = true
	) UserAuthProfile authProfile;


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "pub_id",
			unique = true
	) UserPublicProfile publicProfile;


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "priv_id",
			unique = true
	) UserPrivateProfile privateProfile;


}