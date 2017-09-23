package net.henryco.blinckserver.mvc.model.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h1>User Auth profile JSON</h1>
 * <h2>
 *     {&nbsp;
 *         "id":			LONG, 		&nbsp;
 *         "locked":		BOOLEAN,	&nbsp;
 *         "authorities":	CHAR[255]
 *     &nbsp;}
 * </h2>
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
	) String authorities;


	public @Override @JsonIgnore
	String getPassword() {
		// Users don't use internal passwords
		return null;
	}


	public @Override @JsonIgnore
	boolean isEnabled() {
		// Always enabled, but might be locked
		return true;
	}


	public @Override @JsonIgnore
	boolean isExpired() {
		// Never expired, because based on FB
		return false;
	}
}