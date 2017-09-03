package net.henryco.blinckserver.integration.security.endpoint;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import org.junit.Assert;
import org.junit.Test;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointUnAuthorizedTest extends BlinckIntegrationAccessTest {


	@Test
	public void testRootEndPointUnauthorized() {

		Assert.assertNotEquals(FORBIDDEN.value(), fastGetRequest(ROOT_ENDPOINT).getStatusCode().value());
		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(randomRootPath()).getStatusCode().value());
	}



	@Test
	public void testPublicEndPointUnauthorized() {

		Assert.assertNotEquals(FORBIDDEN.value(), fastGetRequest(PUBLIC_ENDPOINT).getStatusCode().value());
		Assert.assertNotEquals(FORBIDDEN.value(), fastGetRequest(randomPublicPath()).getStatusCode().value());
	}



	@Test
	public void testProtectedEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(PROTECTED_ENDPOINT).getStatusCode().value());
		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(randomProtectedPath()).getStatusCode().value());
	}



	@Test
	public void testUserEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(USER_ENDPOINT).getStatusCode().value());
		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(randomUserPath()).getStatusCode().value());
	}



	@Test
	public void testAdminEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(ADMIN_ENDPOINT).getStatusCode().value());
		Assert.assertEquals(FORBIDDEN.value(), fastGetRequest(randomAdminPath()).getStatusCode().value());
	}
}
