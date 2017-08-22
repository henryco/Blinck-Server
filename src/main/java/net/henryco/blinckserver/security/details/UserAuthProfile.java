package net.henryco.blinckserver.security.details;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.Utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Henry on 22/08/17.
 */

@Entity @Data @NoArgsConstructor
public class UserAuthProfile {

	@Column @Id private long id;
	@Column(nullable = false) private String password;
	@Column(nullable = false) private boolean enabled;
	@Column(nullable = false) private boolean locked;
	@Column(nullable = false) private boolean expired;

	@Column private String authorities;


	public UserAuthProfile(long id, String password, String ... authorities) {
		this.id = id;
		this.password = password;
		this.authorities = Utils.arrayToString(authorities);
	}


	@Deprecated
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String[] getAuthoritiesArray() {
		return Utils.stringToArray(authorities);
	}

	public void setAuthorityArray(String ... authorities) {
		this.authorities = Utils.arrayToString(authorities);
	}

}