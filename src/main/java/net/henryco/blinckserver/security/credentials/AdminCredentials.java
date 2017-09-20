package net.henryco.blinckserver.security.credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>Admin credentials JSON</h1>
 * <h2>
 *     {&nbsp;
 *         "user_id":		CHAR[255],	&nbsp;
 *         "password":		CHAR[255]
 *     &nbsp;}
 * </h2>
 * @author Henry on 24/08/17.
 */
@AllArgsConstructor @NoArgsConstructor
public final class AdminCredentials extends JWTLoginCredentials {

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
