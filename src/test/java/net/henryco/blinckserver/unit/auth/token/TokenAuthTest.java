package net.henryco.blinckserver.unit.auth.token;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.utils.TestedLoop;
import org.springframework.security.core.userdetails.UserDetailsService;

import static net.henryco.blinckserver.utils.TestUtils.randomNumberString;

/**
 * @author Henry on 27/08/17.
 */

public abstract class TokenAuthTest extends BlinckUnitTest {

	private static final class RandomSecretTokenService extends TokenAuthenticationService {

		private String header;
		private String prefix;
		private String role;
		private final String secret;
		private final Long expTime;

		public RandomSecretTokenService(String secret,
										Long expTime) {
			this.secret = secret;
			this.expTime = expTime;
		}

		private RandomSecretTokenService setHeader(String header) {
			this.header = header;
			return this;
		}

		private RandomSecretTokenService setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		private RandomSecretTokenService setDefaultRole(String role) {
			this.role = role;
			return this;
		}

		@Override
		protected UserDetailsService getUserDetailsService() {
			return null;
		}

		@Override
		protected String getTokenSecret() {
			return secret;
		}

		@Override
		protected Long getExpirationTime() {
			return expTime;
		}

		@Override
		protected String getTokenHeader() {
			return header == null ? super.getTokenHeader() : header;
		}

		@Override
		protected String getTokenPrefix() {
			return prefix == null ? super.getTokenPrefix() : prefix;
		}

		@Override
		protected String getDefaultRole() {
			return role == null ? super.getDefaultRole() : role;
		}

	}




	protected static final TestedLoop testLoop = new TestedLoop(100);


	// TODO: 28/08/17       REPLACE WITH BUILDER !!!   vvv

	protected static TokenAuthenticationService createJwtService(Long expTime) {
		return new RandomSecretTokenService(
				randomNumberString(),
				expTime
		);
	}

	protected static TokenAuthenticationService createJwtService() {
		return createJwtService(1_000_000L);
	}

	protected static TokenAuthenticationService createJwtService(Long expTime,
																 String header,
																 String prefix) {
		return new RandomSecretTokenService(randomNumberString(), expTime)
				.setHeader(header).setPrefix(prefix);
	}

	protected static TokenAuthenticationService createJwtService(Long expTime,
																 String header,
																 String prefix,
																 String role) {
		return new RandomSecretTokenService(randomNumberString(), expTime)
				.setHeader(header).setPrefix(prefix).setDefaultRole(role);
	}

	protected static TokenAuthenticationService createJwtService(Long expTime, String header) {
		return new RandomSecretTokenService(randomNumberString(), expTime).setHeader(header);
	}

	protected static TokenAuthenticationService createJwtService(String header) {
		return createJwtService(1_000_000L, header);
	}


}