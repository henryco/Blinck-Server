package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.BlinckAuthorityEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Henry on 22/08/17.
 */

@Entity @Data
@NoArgsConstructor
@Table(name = "USER_PROFILE_AUTH")
public class UserAuthProfile implements BlinckAuthorityEntity {

	@Column @Id private long id;


	@Column(nullable = false) private boolean enabled;
	@Column(nullable = false) private boolean locked;
	@Column(nullable = false) private boolean expired;

	@Column private String authorities;
	@Column private String password;


}