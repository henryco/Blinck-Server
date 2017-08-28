package net.henryco.blinckserver.integration.security.endpoint;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import org.junit.Test;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointAuthorizedAsAdminTest extends BlinckIntegrationAccessTest {


	@Test
	public void testRootEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomRootPath(), authorization).getStatusCode();
	}



	@Test
	public void testPublicEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN == authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode();
	}



	@Test
	public void testUserEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN == authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomUserPath(), authorization).getStatusCode();
	}



	@Test
	public void testAdminEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomAdminPath(), authorization).getStatusCode();
	}

}
