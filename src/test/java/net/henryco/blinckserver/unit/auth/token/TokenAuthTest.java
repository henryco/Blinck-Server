package net.henryco.blinckserver.unit.auth.token;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.utils.TestedLoop;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;

/**
 * @author Henry on 27/08/17.
 */

public abstract class TokenAuthTest extends BlinckUnitTest {

	private static final class RandomSecretTokenService extends TokenAuthenticationService {

		private String header;
		private String prefix;
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
	}

	protected static final TestedLoop testLoop = new TestedLoop(100);

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

	protected static TokenAuthenticationService createJwtService(Long expTime, String header) {
		return new RandomSecretTokenService(randomNumberString(), expTime).setHeader(header);
	}

	protected static TokenAuthenticationService createJwtService(String header) {
		return createJwtService(1_000_000L, header);
	}


}