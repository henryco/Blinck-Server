package net.henryco.blinckserver.security.jwt.credentials;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Henry on 22/08/17.
 */
public final class LoginFacebookCredentials extends JWTLoginCredentials {

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