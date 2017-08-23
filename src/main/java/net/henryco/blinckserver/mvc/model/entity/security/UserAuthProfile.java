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


	private @Id @Column(
			name = "auth_id",
			unique = true
	) long id;


	private @Column(
			nullable = false
	) boolean enabled;


	private @Column(
			nullable = false
	) boolean locked;


	private @Column(
			nullable = false
	) boolean expired;


	private @Column(
			nullable = false
	) String authorities;


	@Column private String password;


}