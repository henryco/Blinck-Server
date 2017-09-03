package net.henryco.blinckserver.integration.security.endpoint;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import org.junit.Test;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointAuthorizedAsUserTest extends BlinckIntegrationAccessTest {



	@Test
	public void testRootEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomRootPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testPublicEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testUserEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN.value() != authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() != authorizedGetRequest(randomUserPath(), authorization).getStatusCode().value();
	}



	@Test
	public void testAdminEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN.value() == authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode().value();
		assert FORBIDDEN.value() == authorizedGetRequest(randomAdminPath(), authorization).getStatusCode().value();
	}

}