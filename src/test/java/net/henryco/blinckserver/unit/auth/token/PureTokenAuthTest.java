package net.henryco.blinckserver.unit.auth.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * @author Henry on 27/08/17.
 */
public class PureTokenAuthTest extends TokenAuthTest {

	private static final Long tokenExpTime = 1_000_000L;


	@Test
	public void createdTokenIsNotSimilarTest() throws Exception {

		testLoop.test(() -> {
			final String value = TestUtils.randomGaussNumberString();
			assert !getCoderMethod().invoke(createJwtService(new Random().nextLong()), value).equals(value);
		});
	}


	@Test
	public void tokenSingleServiceCodeDeCodeTest() throws Exception {

		TokenAuthenticationService service = createJwtService(tokenExpTime);

		final Method deCoder = getDeCoderMethod();
		final Method coder = getCoderMethod();

		testLoop.test(() -> {
			final String value = TestUtils.randomGaussNumberString();
			assert deCoder.invoke(service, coder.invoke(service, value)).equals(value);
		});
	}


	@Test
	public void tokenMultiServiceCodeDeCodeSimilarTest() throws Exception {

		testLoop.test(() -> {

			final String name = TestUtils.randomGaussNumberString();
			String coded = getCoderMethod().invoke(createJwtService(tokenExpTime), name).toString();

			try {
				getDeCoderMethod().invoke(createJwtService(tokenExpTime), coded);
			} catch (InvocationTargetException e) {
				assert e.getTargetException() instanceof SignatureException;
			}
		});
	}


	@Test
	public void createdTokenMultiServiceIsNotSimilar() throws Exception {

		testLoop.test(() -> {
			final String value = TestUtils.randomGaussNumberString();

			final String tokenOne = getCoderMethod().invoke(createJwtService(tokenExpTime), value).toString();
			final String tokenTwo = getCoderMethod().invoke(createJwtService(tokenExpTime), value).toString();

			assert !tokenOne.equals(tokenTwo);
		});
	}



	@Test
	public void tokenExpirationTimeTest() throws Exception {

		final long expTimeMs = 10L;
		final TokenAuthenticationService service = createJwtService(expTimeMs);
		final Object token = getCoderMethod().invoke(service, TestUtils.randomGaussNumberString());

		for (int i = 0; i < 10; i++) {

			Thread.sleep(expTimeMs + 1);

			try {
				getDeCoderMethod().invoke(service, token);
			} catch (InvocationTargetException e) {
				assert e.getTargetException() instanceof ExpiredJwtException;
			}
		}
	}



	@Test @SuppressWarnings("unchecked")
	public void tokenDefaultRolGrantTest() throws Exception {

		testLoop.test(() -> {

			final String DEFAULT_ROLE = "ROLE_"+ TestUtils.randomGaussNumberString();

			Collection<? extends GrantedAuthority> authorities =
					(Collection<? extends GrantedAuthority>)
							getDefaultAuthorityGranterMethod().invoke(getJwtService(DEFAULT_ROLE));

			assert !authorities.isEmpty();
			assert authorities.stream().anyMatch(auth -> auth.getAuthority().equals(DEFAULT_ROLE));
		});
	}



	@Test @SuppressWarnings("unchecked")
	public void grantAuthoritiesTest() throws Exception {

		testLoop.test(() -> {

			final String DEFAULT_ROLE = "ROLE_"+ TestUtils.randomGaussNumberString();

			final String[] auth = new String[] {
					"ROLE_" + TestUtils.randomGaussNumberString(),
					"ROLE_" + TestUtils.randomGaussNumberString(),
			};

			Collection<? extends GrantedAuthority> roles =
					(Collection<? extends GrantedAuthority>)
							getAuthorityGranterMethod().invoke(getJwtService(DEFAULT_ROLE), (Object) auth);

			Arrays.stream(auth).forEach(a -> {
				assert roles.stream().anyMatch(g -> g.getAuthority().equals(a));
			});
		});
	}



	private static Method getCoderMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"parseTo"
		);
	}


	private static Method getDeCoderMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"parseFrom"
		);
	}


	private static Method getAuthorityGranterMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"grantAuthorities"
		);
	}


	private static Method getDefaultAuthorityGranterMethod() {
		return BlinckTestUtil.getMethod(
				TokenAuthenticationService.class,
				"grantDefaultAuthorities"
		);
	}


	private static TokenAuthenticationService getJwtService(String DEFAULT_ROLE) {
		return createJwtService(
				tokenExpTime,
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString(),
				DEFAULT_ROLE
		);
	}
}