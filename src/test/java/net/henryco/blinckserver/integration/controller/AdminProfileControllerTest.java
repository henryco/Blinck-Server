package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import net.henryco.blinckserver.utils.JsonForm;
import net.henryco.blinckserver.utils.MockFacebookUser;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.social.facebook.api.User;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;

/**
 * @author Henry on 01/09/17.
 */
public class AdminProfileControllerTest extends BlinckIntegrationAccessTest {

	protected static final String REGISTRATION = ADMIN_ENDPOINT + "/registration";
	protected static final String LIST = ADMIN_ENDPOINT + "/list";
	protected static final String VERIFICATION = ADMIN_ENDPOINT + "/verification?size=10000000";
	protected static final String ACTIVATION = ADMIN_ENDPOINT + "/activate";
	protected static final String PERMISSIONS = ADMIN_ENDPOINT + "/permissions";
	protected static final String LOGOUT = ADMIN_ENDPOINT + "/session/close";
	protected static final String ROLE_ADD = ADMIN_ENDPOINT + "/authority/add";
	protected static final String ROLE_REMOVE = ADMIN_ENDPOINT + "/authority/remove";



	@Test
	public void adminRegistrationTest() {
		assert fastPostRequest(REGISTRATION, createRandomAdmin()).getStatusCode().is2xxSuccessful();
	}



	@Test
	public void adminListTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);

		final String adminAuthToken = getForAdminAuthToken();
		String[] admins = authorizedGetRequest(LIST, adminAuthToken, String[].class).getBody();

		assert Arrays.stream(admins).anyMatch(form.user_id::equals);
	}



	@Test
	public void adminVerificationListTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		final String adminAuthToken = getForAdminAuthToken();

		String[] verification = authorizedGetRequest(VERIFICATION, adminAuthToken, String[].class).getBody();

		assert Arrays.stream(verification).anyMatch(form.user_id::equals);
	}



	@Test
	public void adminActivationTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		final String adminAuthToken = getForAdminAuthToken();

		String[] activationList = {form.user_id};

		assert fastPostRequest(ACTIVATION, activationList).getStatusCode().is4xxClientError();
		assert authorizedPostRequest(ACTIVATION, adminAuthToken, activationList).getStatusCode().is2xxSuccessful();

		String[] verification = authorizedGetRequest(VERIFICATION, adminAuthToken, String[].class).getBody();
		assert Arrays.stream(verification).noneMatch(form.user_id::equals);
	}



	@Test
	public void adminLoginUnVerifiedTest() {

		try {
			fastPostRequest(LOGIN_ENDPOINT_ADMIN, registerRandomAdmin(this)).getStatusCode();
			assert false;
		} catch (ResourceAccessException e) {
			assert true;
		}
	}


	@Test
	public void adminLoginVerifiedTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		activateAdmin(this, form);

		assert fastPostRequest(LOGIN_ENDPOINT_ADMIN, form).getStatusCode().is2xxSuccessful();
	}







	@Test
	public void adminLogoutAdminTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		final String logoutRequest = LOGOUT + "/admin?name=" + form.user_id;

		activateAdmin(this, form);

		String token = customAdminLogin(this, form);
		assert authorizedGetRequest(PERMISSIONS, token).getStatusCode().is2xxSuccessful();

		authorizedGetRequest(logoutRequest, getForAdminAuthToken());
		assert authorizedGetRequest(PERMISSIONS, token).getStatusCode().is4xxClientError();
	}



	@Test
	public void adminLogoutUserTest() throws Exception {

		final User user = MockFacebookUser.getInstance().createRandom().getUser();
		final String userAuthToken = getForUserAuthToken(user);

		final String request = USER_ENDPOINT + "/permissions";
		assert authorizedGetRequest(request, userAuthToken).getStatusCode().is2xxSuccessful();

		final String logoutRequest = LOGOUT + "/user?name=" + user.getId();
		authorizedGetRequest(logoutRequest, getForAdminAuthToken());

		assert authorizedGetRequest(request, userAuthToken).getStatusCode().is4xxClientError();
	}



	@Test
	public void adminAddAuthorityTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		activateAdmin(this, form);

		String token = customAdminLogin(this, form);
		final String[] permsBefore = authorizedGetRequest(PERMISSIONS, token, String[].class).getBody();

		final String randPerm = "ROLE_"+TestUtils.randomGaussNumberString();
		final String request = ROLE_ADD + "?name="+form.user_id + "&role="+randPerm;

		authorizedGetRequest(request, getForAdminAuthToken());

		assert authorizedGetRequest(PERMISSIONS, token).getStatusCode().is4xxClientError();

		token = customAdminLogin(this, form);
		final String[] permsAfter = authorizedGetRequest(PERMISSIONS, token, String[].class).getBody();

		assert !Arrays.toString(permsBefore).equals(Arrays.toString(permsAfter));
		assert Arrays.stream(permsBefore).noneMatch(randPerm::equals);
		assert Arrays.stream(permsAfter).anyMatch(randPerm::equals);
	}



	@Test
	public void adminRemoveAuthorityTest() {

		final JsonForm.AdminLoginPost form = registerRandomAdmin(this);
		activateAdmin(this, form);

		String token = customAdminLogin(this, form);
		final String[] permsBefore = authorizedGetRequest(PERMISSIONS, token, String[].class).getBody();

		final String randPerm = "ROLE_USER";
		final String request = ROLE_REMOVE + "?name="+form.user_id + "&role="+randPerm;

		authorizedGetRequest(request, getForAdminAuthToken());

		assert authorizedGetRequest(PERMISSIONS, token).getStatusCode().is4xxClientError();

		token = customAdminLogin(this, form);
		final String[] permsAfter = authorizedGetRequest(PERMISSIONS, token, String[].class).getBody();

		assert !Arrays.toString(permsBefore).equals(Arrays.toString(permsAfter));
		assert Arrays.stream(permsBefore).anyMatch(randPerm::equals);
		assert Arrays.stream(permsAfter).noneMatch(randPerm::equals);
	}





	private static
	JsonForm.AdminLoginPost createRandomAdmin() {
		return new JsonForm.AdminLoginPost(
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString()
		);
	}


	private static
	JsonForm.AdminLoginPost registerRandomAdmin(AdminProfileControllerTest context) {
		final JsonForm.AdminLoginPost form = createRandomAdmin();
		context.fastPostRequest(REGISTRATION, form);
		return form;
	}


	private static
	String customAdminLogin(AdminProfileControllerTest context,
							JsonForm.AdminLoginPost form) {
		return context.fastPostRequest(LOGIN_ENDPOINT_ADMIN, form)
				.getHeaders().get(HEADER_ACCESS_TOKEN_NAME).get(0);
	}


	private static
	void activateAdmin(AdminProfileControllerTest context,
					   JsonForm.AdminLoginPost form) {
		context.authorizedPostRequest(ACTIVATION,
				context.getForAdminAuthToken(),
				new String[]{form.user_id}
		);
	}

}