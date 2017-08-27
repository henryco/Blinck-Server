package net.henryco.blinckserver.unit.auth;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.HTTPTestUtils;
import net.henryco.blinckserver.utils.TestedLoop;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Random;

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






public class TokenAuthTest extends BlinckUnitTest {

	private static final TestedLoop testLoop = new TestedLoop(100);

	private static TokenAuthenticationService createService(Long expTime) {
		return new RandomSecretTokenService(
				HTTPTestUtils.randomNumberString(),
				expTime
		);
	}

	private static Method getCoderMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"createAuthenticationToken"
		);
	}

	private static Method getDeCoderMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"readAuthenticationToken"
		);
	}



	MockHttpServletRequest request = new MockHttpServletRequest();



	@Test
	public void createdTokenSimilarityTest() throws Exception {

		final Method coder = getCoderMethod();
		final String value = HTTPTestUtils.randomNumberString();

		testLoop.test(() -> {
			assert !coder.invoke(createService(new Random().nextLong()), value).equals(value);
		});
	}
	
	@Test
	public void tokenSingleServiceCodeDeCodeTest() throws Exception {

		TokenAuthenticationService service = createService(1_000_000L);

		final Method coder = getCoderMethod();
		final Method deCoder = getDeCoderMethod();

		final String value = HTTPTestUtils.randomNumberString();

		testLoop.test(() -> {
			assert deCoder.invoke(service, coder.invoke(service, value)).equals(value);
		});
	}







}