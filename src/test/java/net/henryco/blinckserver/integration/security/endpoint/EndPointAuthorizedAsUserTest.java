package net.henryco.blinckserver.integration.security.endpoint;

import org.junit.Test;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointAuthorizedAsUserTest extends EndPointAccessTest {



	@Test
	public void testRootEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomRootPath(), authorization).getStatusCode();
	}



	@Test
	public void testPublicEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode();
	}



	@Test
	public void testUserEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomUserPath(), authorization).getStatusCode();
	}



	@Test
	public void testAdminEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN == authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomAdminPath(), authorization).getStatusCode();
	}

}