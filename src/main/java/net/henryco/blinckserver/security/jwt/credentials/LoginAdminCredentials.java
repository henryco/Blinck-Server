package net.henryco.blinckserver.security.jwt.credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Henry on 24/08/17.
 */
@AllArgsConstructor @NoArgsConstructor
public final class LoginAdminCredentials extends JWTLoginCredentials {

	@Getter @Setter private String user_id;
	@Getter @Setter private String password;

	@Override
	public String getPrincipal() {
		return user_id;
	}

	@Override
	public String getCredentials() {
		return password;
	}
}
