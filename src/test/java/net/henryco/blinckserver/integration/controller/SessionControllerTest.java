package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.junit.Test;
import org.springframework.social.facebook.api.User;

import java.io.Serializable;

/**
 * @author Henry on 03/09/17.
 */
public class SessionControllerTest extends BlinckIntegrationAccessTest {

	private static final String DEFAULT_PRINCIPAL = "unknown";
	private static final String SESSION_USER = "/session/user";
	private static final String SESSION_ADMIN = "/session/admin";

	private static final String LOGOUT_USER = SESSION_USER + "/logout";
	private static final String LOGOUT_ADMIN = SESSION_ADMIN + "/logout";

	private static final class TestStatus
			implements Serializable {
		public String principal;
		public Boolean active;
	}



	@Test
	public void sessionStatusWithoutTokenTest() {

		TestStatus status = fastGetRequest(SESSION_USER, TestStatus.class).getBody();
		assert status.principal.equals(DEFAULT_PRINCIPAL);
		assert !status.active;
	}



	@Test
	public void sessionStatusUser() throws Exception {

		final User user = MockFacebookUser.getInstance().createRandom().getUser();
		final String token = getForUserAuthToken(user);

		TestStatus status = authorizedGetRequest(SESSION_USER, token, TestStatus.class).getBody();
		assertionStatus(status, user.getId(), true);
	}



	@Test
	public void sessionStatusAdminTest() {

		final String token = getForAdminAuthToken();
		final String name = environment.getProperty("security.default.admin.name");

		TestStatus status = authorizedGetRequest(SESSION_ADMIN, token, TestStatus.class).getBody();
		assertionStatus(status, name, true);
	}



	@Test
	public void sessionLogoutUserTest() throws Exception {

		final String token = getForUserAuthToken();
		authorizedGetRequest(LOGOUT_USER, token);

		TestStatus status = authorizedGetRequest(SESSION_USER, token, TestStatus.class).getBody();
		assertionStatus(status, DEFAULT_PRINCIPAL, false);
	}



	@Test
	public void sessionLogoutAdminTest() {

		final String token = getForAdminAuthToken();
		authorizedGetRequest(LOGOUT_ADMIN, token);

		TestStatus status = authorizedGetRequest(SESSION_ADMIN, token, TestStatus.class).getBody();
		assertionStatus(status, DEFAULT_PRINCIPAL, false);
	}



	private static void
	assertionStatus(TestStatus status, String name, boolean active) {
		assert name.equals(status.principal);
		assert status.active == active;
	}

}