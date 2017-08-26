package net.henryco.blinckserver.integration.security.endpoint;

import org.junit.Assert;
import org.junit.Test;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointUnAuthorizedTest extends EndPointAccessTest {


	@Test
	public void testRootEndPointUnauthorized() {

		Assert.assertNotEquals(FORBIDDEN, fastGetRequest(ROOT_ENDPOINT).getStatusCode());
		Assert.assertEquals(FORBIDDEN, fastGetRequest(randomRootPath()).getStatusCode());
	}



	@Test
	public void testPublicEndPointUnauthorized() {

		Assert.assertNotEquals(FORBIDDEN, fastGetRequest(PUBLIC_ENDPOINT).getStatusCode());
		Assert.assertNotEquals(FORBIDDEN, fastGetRequest(randomPublicPath()).getStatusCode());
	}



	@Test
	public void testProtectedEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN, fastGetRequest(PROTECTED_ENDPOINT).getStatusCode());
		Assert.assertEquals(FORBIDDEN, fastGetRequest(randomProtectedPath()).getStatusCode());
	}



	@Test
	public void testUserEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN, fastGetRequest(USER_ENDPOINT).getStatusCode());
		Assert.assertEquals(FORBIDDEN, fastGetRequest(randomUserPath()).getStatusCode());
	}



	@Test
	public void testAdminEndPointUnauthorized() {

		Assert.assertEquals(FORBIDDEN, fastGetRequest(ADMIN_ENDPOINT).getStatusCode());
		Assert.assertEquals(FORBIDDEN, fastGetRequest(randomAdminPath()).getStatusCode());
	}
}
