package net.henryco.blinckserver.unit.auth;

import io.jsonwebtoken.SignatureException;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.TestedLoop;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Random;

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






public class TokenAuthTest extends BlinckUnitTest {

	private static final TestedLoop testLoop = new TestedLoop(100);
	private static final Long tokenExpTime = 1_000_000L;

	private static TokenAuthenticationService createService(Long expTime) {
		return new RandomSecretTokenService(
				randomNumberString(),
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
	public void createdTokenIsNotSimilarTest() throws Exception {

		final Method coder = getCoderMethod();
		final String value = randomNumberString();

		testLoop.test(() -> {
			assert !coder.invoke(createService(new Random().nextLong()), value).equals(value);
		});
	}



	@Test
	public void tokenSingleServiceCodeDeCodeTest() throws Exception {

		TokenAuthenticationService service = createService(tokenExpTime);

		final Method deCoder = getDeCoderMethod();
		final Method coder = getCoderMethod();

		final String value = randomNumberString();

		testLoop.test(() -> {
			assert deCoder.invoke(service, coder.invoke(service, value)).equals(value);
		});
	}



	@Test
	public void tokenMultiServiceCodeDeCodeTest() throws Exception {

		testLoop.test(() -> {

			String coded = getCoderMethod().invoke(createService(tokenExpTime), randomNumberString()).toString();

			try {
				getDeCoderMethod().invoke(createService(tokenExpTime), coded);
			} catch (InvocationTargetException e) {
				assert e.getTargetException() instanceof SignatureException;
			}

		});

	}


}