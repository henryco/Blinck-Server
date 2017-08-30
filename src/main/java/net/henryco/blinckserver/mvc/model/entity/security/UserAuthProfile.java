package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Henry on 22/08/17.
 */

@Entity @Data
@NoArgsConstructor
@Proxy(lazy = false)
@Table(name = "USER_PROFILE_AUTH")
public class UserAuthProfile implements BlinckAuthorityEntity<Long> {


	private @Id @Column(
			name = "auth_id",
			unique = true
	) Long id;


	private @Column(
			nullable = false
	) boolean locked;


	private @Column(
			nullable = false
	) boolean expired;


	private @Column(
			nullable = false
	) String authorities;


	public @Override
	String getPassword() {
		// Users don't use internal passwords
		return null;
	}


	public @Override
	boolean isEnabled() {
		// Always enabled, but might be locked
		return true;
	}

}