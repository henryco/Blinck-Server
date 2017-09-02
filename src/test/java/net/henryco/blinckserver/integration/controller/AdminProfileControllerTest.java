package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import net.henryco.blinckserver.utils.JsonForm;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;

/**
 * @author Henry on 01/09/17.
 */
public class AdminProfileControllerTest extends BlinckIntegrationAccessTest {

	protected static final String REGISTRATION = ADMIN_ENDPOINT + "/registration";
	protected static final String LIST = ADMIN_ENDPOINT + "/list";
	protected static final String VERIFICATION = ADMIN_ENDPOINT + "/verification?size=10000000";
	protected static final String ACTIVATION = ADMIN_ENDPOINT + "/activate/admin";
	protected static final String PERMISSIONS = ADMIN_ENDPOINT + "/permissions";
	protected static final String LOGOUT = ADMIN_ENDPOINT + "/session/logout";


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
		authorizedPostRequest(ACTIVATION, getForAdminAuthToken(), new String[]{form.user_id});

		assert fastPostRequest(LOGIN_ENDPOINT_ADMIN, form).getStatusCode().is2xxSuccessful();
	}



	@Test
	public void adminLogoutSelfTest() {

		final String adminAuthToken = getForAdminAuthToken();

		assert authorizedGetRequest(PERMISSIONS, adminAuthToken).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(LOGOUT, adminAuthToken).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(PERMISSIONS, adminAuthToken).getStatusCode().is4xxClientError();
	}




}