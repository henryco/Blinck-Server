package net.henryco.blinckserver.security.credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Henry on 22/08/17.
 */
@AllArgsConstructor @NoArgsConstructor
public final class FacebookCredentials extends JWTLoginCredentials {

	@Getter @Setter
	private String facebook_uid;

	@Getter @Setter
	private String facebook_token;

	@Override
	public String getPrincipal() {
		return facebook_uid;
	}

	@Override
	public String getCredentials() {
		return facebook_token;
	}
}