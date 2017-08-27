package net.henryco.blinckserver.unit.auth.token;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Random;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;

/**
 * @author Henry on 27/08/17.
 */
public class HttpTokenAuthTest extends TokenAuthTest {



	@Test
	public void tokenCreationTest() throws Exception {

		testLoop.test(() -> {

			final String authTokenHeader = randomNumberString();

			TokenAuthenticationService service = createJwtService(authTokenHeader);
			HttpServletResponse response = createJwtResponse(service, randomNumberString());

			assert !response.getHeader(authTokenHeader).isEmpty();
		});
	}



	@Test
	public void tokenPrefixTest() throws Exception {

		testLoop.test(() -> {

			final String header = randomNumberString();
			final String prefix = randomNumberString();
			final Long expTime = new Random().nextLong();

			TokenAuthenticationService service = createJwtService(expTime, header, prefix);
			HttpServletResponse response = createJwtResponse(service, randomNumberString());

			assert response.getHeader(header).contains(prefix);
		});
	}



	@Test
	public void tokenDeCodeAuthenticationTest() throws Exception {

		testLoop.test(() -> {

			final String principal = randomNumberString();
			final String header = randomNumberString();
			final String prefix = randomNumberString();
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
			final String header = randomNumberString();

			TokenAuthenticationService service = createJwtService(expTimeMs, header);
			HttpServletResponse jwtResponse = createJwtResponse(service, randomNumberString());

			Thread.sleep(expTimeMs + 1);

			HttpServletRequest jwtRequest = createJwtRequest(header, jwtResponse.getHeader(header));
			assert service.getAuthentication(jwtRequest) == null;
		});
	}




	private static HttpServletResponse createJwtResponse(TokenAuthenticationService service,
														 String principal) {
		HttpServletResponse response = new MockHttpServletResponse();
		service.addAuthentication(response, principal);
		return response;
	}


	private static HttpServletRequest createJwtRequest(String header, String token) {

		HttpServletRequest request = new MockHttpServletRequest();
		((MockHttpServletRequest) request).addHeader(header, token);
		return request;
	}

}