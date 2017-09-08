package net.henryco.blinckserver.unit.auth.token;

import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static net.henryco.blinckserver.utils.TestUtils.randomGaussNumberString;

/**
 * @author Henry on 27/08/17.
 */
public class HttpTokenAuthTest extends TokenAuthTest {

	private static final String[] ROLES_DEFAULT = {"ROLE_ONE", "ROLE_TWO"};


	@Test
	public void tokenCreationTest() throws Exception {

		testLoop.test(() -> {

			final String authTokenHeader = TestUtils.randomGaussNumberString();

			TokenAuthenticationService service = createJwtService(authTokenHeader);
			HttpServletResponse response = createJwtResponse(service, TestUtils.randomGaussNumberString());

			assert !response.getHeader(authTokenHeader).isEmpty();
		});
	}



	@Test
	public void tokenPrefixTest() throws Exception {

		testLoop.test(() -> {

			final String header = TestUtils.randomGaussNumberString();
			final String prefix = TestUtils.randomGaussNumberString();
			final Long expTime = new Random().nextLong();

			TokenAuthenticationService service = createJwtService(expTime, header, prefix);
			HttpServletResponse response = createJwtResponse(service, TestUtils.randomGaussNumberString());

			assert response.getHeader(header).contains(prefix);
		});
	}



	@Test
	public void tokenDeCodeAuthenticationTest() throws Exception {

		testLoop.test(() -> {

			final String principal = TestUtils.randomGaussNumberString();
			final String header = TestUtils.randomGaussNumberString();
			final String prefix = TestUtils.randomGaussNumberString();
			final Long expTime = 1_000_000_000L;

			TokenAuthenticationService service = createJwtService(expTime, header, prefix);
			HttpServletResponse response = createJwtResponse(service, principal);
			HttpServletRequest request = createJwtRequest(header, response.getHeader(header));

			Authentication authentication = service.getAuthentication(request);

			assert authentication != null;
			assert authentication.getPrincipal().equals(principal);
		});
	}



	@Test
	public void tokenExpiredAuthenticationTest() throws Exception {

		testLoop.test(() -> {

			final Long expTimeMs = (long) Math.abs(new Random().nextGaussian() * 10D);
			final String header = TestUtils.randomGaussNumberString();

			TokenAuthenticationService service = createJwtService(expTimeMs, header);
			HttpServletResponse jwtResponse = createJwtResponse(service, TestUtils.randomGaussNumberString());

			Thread.sleep(expTimeMs + 1);

			HttpServletRequest jwtRequest = createJwtRequest(header, jwtResponse.getHeader(header));
			assert service.getAuthentication(jwtRequest) == null;
		});
	}




	private static HttpServletResponse
	createJwtResponse(TokenAuthenticationService service,
														 String principal) {
		HttpServletResponse response = new MockHttpServletResponse();
		Authentication auth = createAuthentication(principal, ROLES_DEFAULT);
		service.addAuthentication(response, auth);
		return response;
	}


	private static HttpServletRequest
	createJwtRequest(String header, String token) {

		HttpServletRequest request = new MockHttpServletRequest();
		((MockHttpServletRequest) request).addHeader(header, token);
		return request;
	}


	private static Authentication
	createAuthentication(String name, String... roles) {
		return new UsernamePasswordAuthenticationToken(
				name, null, Arrays.stream(roles)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())
		);
	}


}