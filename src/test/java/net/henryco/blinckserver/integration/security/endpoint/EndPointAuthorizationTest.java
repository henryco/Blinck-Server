package net.henryco.blinckserver.integration.security.endpoint;

import org.junit.Test;

/**
 * @author Henry on 27/08/17.
 */
public class EndPointAuthorizationTest extends EndPointAccessTest {


	@Test
	public void testPublicAdminAuthorization() {
		assert getForAdminAuthToken() != null;
	}


	@Test
	public void testPublicUserAuthorization() throws Exception {
		assert getForUserAuthToken() != null;
	}

}