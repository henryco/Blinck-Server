package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import org.junit.Test;

/**
 * @author Henry on 28/08/17.
 */
public class BlinckProfileControllerTest extends BlinckIntegrationAccessTest {

	private static final String USER_ENDPOINT = "/protected/user";
	private static final String ADMIN_ENDPOINT = "/protected/admin";

	private static final String PERMISSIONS_PATH_POSTFIX = "/permissions";
	private static final String PROFILE_PATH_POSTFIX = "/profile";

	@Test
	public void profilePermissionsTest() throws Exception {

		final String USER_PERMISSIONS = USER_ENDPOINT + PERMISSIONS_PATH_POSTFIX;
		final String ADMIN_PERMISSIONS = ADMIN_ENDPOINT + PERMISSIONS_PATH_POSTFIX;

		assert authorizedGetRequest(USER_PERMISSIONS, getForUserAuthToken(), String[].class).getBody().length >= 1;
		assert authorizedGetRequest(ADMIN_PERMISSIONS, getForAdminAuthToken(), String[].class).getBody().length >= 1;
	}

	@Test
	public void profileIndexTest() throws Exception {

		final String USER_PROFILE = USER_ENDPOINT + PROFILE_PATH_POSTFIX;
		final String ADMIN_PROFILE = ADMIN_ENDPOINT + PROFILE_PATH_POSTFIX;

		assert authorizedGetRequest(USER_PROFILE, getForUserAuthToken()).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(ADMIN_PROFILE, getForAdminAuthToken()).getStatusCode().is2xxSuccessful();
	}

}