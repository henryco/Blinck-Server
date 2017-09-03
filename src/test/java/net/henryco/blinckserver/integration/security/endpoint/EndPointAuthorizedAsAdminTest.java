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

		assert FORBIDDEN.value() != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() == authorizedGetRequest(randomRootPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testPublicEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN.value() == authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() == authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testUserEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN.value() == authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() == authorizedGetRequest(randomUserPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testAdminEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomAdminPath(), authorization).getStatusCode().value();
	}

}
