package net.henryco.blinckserver.security.jwt.credentials;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Henry on 24/08/17.
 */
public final class LoginAdminCredentials extends JWTLoginCredentials {

	@Getter @Setter private String username;
	@Getter @Setter private String password;

	@Override
	public String getPrincipal() {
		return username;
	}

	@Override
	public String getCredentials() {
		return password;
	}
}
