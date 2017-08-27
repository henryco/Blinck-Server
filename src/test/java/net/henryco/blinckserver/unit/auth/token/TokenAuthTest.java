package net.henryco.blinckserver.unit.auth.token;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.utils.TestedLoop;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;

/**
 * @author Henry on 27/08/17.
 */



final class RandomSecretTokenService extends TokenAuthenticationService {

	private final String secret;
	private final Long expTime;

	public RandomSecretTokenService(String secret,
									Long expTime) {
		this.secret = secret;
		this.expTime = expTime;
	}

	@Override
	protected String getTokenSecret() {
		return secret;
	}

	@Override
	protected Long getExpirationTime() {
		return expTime;
	}
}


public abstract class TokenAuthTest extends BlinckUnitTest {

	protected static final TestedLoop testLoop = new TestedLoop(100);

	protected static TokenAuthenticationService createService(Long expTime) {
		return new RandomSecretTokenService(
				randomNumberString(),
				expTime
		);
	}

}