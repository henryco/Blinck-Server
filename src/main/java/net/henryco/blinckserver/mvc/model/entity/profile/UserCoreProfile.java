package net.henryco.blinckserver.mvc.model.entity.profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv.PrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.PublicProfile;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

/**
 * @author Henry on 23/08/17.
 */
@Entity @Data
@NoArgsConstructor @Proxy
@Table(name = "user_profile")
public class UserCoreProfile {


	private @Id @Column(
			unique = true,
			name = "user_id"
	) Long id;


	private @OneToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "auth_id",
			unique = true
	) UserAuthProfile authProfile;


	private @Embedded @JoinColumn(
			name = "pub_id"
	) PublicProfile publicProfile;


	private @Embedded @JoinColumn(
			name = "priv_id"
	) PrivateProfile privateProfile;


}