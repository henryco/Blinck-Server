package net.henryco.blinckserver.unit.auth.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;

/**
 * @author Henry on 27/08/17.
 */
public class PureTokenAuthTest extends TokenAuthTest {

	private static final Long tokenExpTime = 1_000_000L;


	@Test
	public void createdTokenIsNotSimilarTest() throws Exception {

		testLoop.test(() -> {
			final String value = randomNumberString();
			assert !getCoderMethod().invoke(createService(new Random().nextLong()), value).equals(value);
		});
	}


	@Test
	public void tokenSingleServiceCodeDeCodeTest() throws Exception {

		TokenAuthenticationService service = createService(tokenExpTime);

		final Method deCoder = getDeCoderMethod();
		final Method coder = getCoderMethod();

		testLoop.test(() -> {
			final String value = randomNumberString();
			assert deCoder.invoke(service, coder.invoke(service, value)).equals(value);
		});
	}


	@Test
	public void tokenMultiServiceCodeDeCodeSimilarTest() throws Exception {

		testLoop.test(() -> {
			String coded = getCoderMethod().invoke(createService(tokenExpTime), randomNumberString()).toString();

			try {
				getDeCoderMethod().invoke(createService(tokenExpTime), coded);
			} catch (InvocationTargetException e) {
				assert e.getTargetException() instanceof SignatureException;
			}
		});
	}


	@Test
	public void createdTokenMultiServiceIsNotSimilar() throws Exception {

		testLoop.test(() -> {
			final String value = randomNumberString();

			final String tokenOne = getCoderMethod().invoke(createService(tokenExpTime), value).toString();
			final String tokenTwo = getCoderMethod().invoke(createService(tokenExpTime), value).toString();

			assert !tokenOne.equals(tokenTwo);
		});
	}



	@Test
	public void tokenExpirationTimeTest() throws Exception {

		final long expTimeMs = 10L;
		final TokenAuthenticationService service = createService(expTimeMs);
		final Object token = getCoderMethod().invoke(service, randomNumberString());

		for (int i = 0; i < 10; i++) {

			Thread.sleep(expTimeMs);

			try {
				getDeCoderMethod().invoke(service, token);
			} catch (InvocationTargetException e) {
				assert e.getTargetException() instanceof ExpiredJwtException;
			}
		}
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

}