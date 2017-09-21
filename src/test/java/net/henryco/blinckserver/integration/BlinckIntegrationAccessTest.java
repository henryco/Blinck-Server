package net.henryco.blinckserver.integration;

import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.security.token.service.SessionWhiteListService;
import net.henryco.blinckserver.security.token.service.imp.UserTokenAuthService;
import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.JsonForm;
import net.henryco.blinckserver.utils.MockFacebookUser;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.User;

import java.lang.reflect.Method;

/**
 * @author Henry on 25/08/17.
 */

@PropertySource("classpath:/static/props/base.properties")
public abstract class BlinckIntegrationAccessTest extends BlinckIntegrationTest {

	protected static final String[] USER_ROLES = {"ROLE_USER"};

	protected static final String HEADER_ACCESS_TOKEN_NAME = "Authorization";
	protected static final String LOGIN_ENDPOINT_ADMIN = "/login/admin";
	protected static final String ROOT_ENDPOINT = "/";
	protected static final String PUBLIC_ENDPOINT = "/public";
	protected static final String PROTECTED_ENDPOINT = "/protected";
	protected static final String ADMIN_ENDPOINT = "/protected/admin";
	protected static final String USER_ENDPOINT = "/protected/user";


	protected @Autowired UserDataService userDataService;
	protected @Autowired UserTokenAuthService userTokenAuthService;
	protected @Autowired SessionWhiteListService whiteListService;


	@Before
	public final void setUp() {
		userDataService.addNewFacebookUserIfNotExist(
				null,
				MockFacebookUser.getInstance().getUser()
		);
	}


	protected ResponseEntity<String> authorizedGetRequest(String endPoint, String token) {
		return authorizedGetRequest(endPoint, token, String.class);
	}

	protected ResponseEntity<String> authorizedPostRequest(String endPoint, String token, Object post) {
		return authorizedPostRequest(endPoint, token, post, String.class);
	}

	protected ResponseEntity<String> authorizedDeleteRequest(String endPoint, String authToken) {
		return authorizedDeleteRequest(endPoint, authToken, String.class);
	}


	protected <T> ResponseEntity<T> authorizedGetRequest(String endPoint,
														 String authToken,
														 Class<T> responseType) {
		return restTemplate.exchange(
				RequestEntity.get(TestUtils.newURI(endPoint, port))
						.header(HEADER_ACCESS_TOKEN_NAME, authToken)
						.accept(MediaType.APPLICATION_JSON)
						.build(),
				responseType
		);
	}


	protected <T> ResponseEntity<T> authorizedPostRequest(String endPoint,
														  String authToken,
														  Object postForm,
														  Class<T> responseType) {
		return restTemplate.exchange(
				RequestEntity.post(TestUtils.newURI(endPoint, port))
						.header(HEADER_ACCESS_TOKEN_NAME, authToken)
						.accept(MediaType.APPLICATION_JSON)
						.body(postForm),
				responseType
		);
	}


	protected <T> ResponseEntity<T> authorizedDeleteRequest(String endPoint,
															String authToken,
															Class<T> responseType) {
		return restTemplate.exchange(
				RequestEntity.delete(TestUtils.newURI(endPoint, port))
						.header(HEADER_ACCESS_TOKEN_NAME, authToken)
						.accept(MediaType.APPLICATION_JSON)
						.build(),
				responseType
		);
	}



	protected String getForAdminAuthToken() {

		return fastPostRequest(
				LOGIN_ENDPOINT_ADMIN, new JsonForm.AdminLoginPost(
						environment.getProperty("security.default.admin.name"),
						environment.getProperty("security.default.admin.password")
				)
		).getHeaders().get(HEADER_ACCESS_TOKEN_NAME).get(0);
	}



	protected String getForUserAuthToken() throws Exception {
		return getForUserAuthToken(MockFacebookUser.getInstance().getUser());
	}

	protected String getForUserAuthToken(User user) throws Exception {
		return getForUserAuthToken(Long.decode(user.getId()));
	}

	protected String getForUserAuthToken(Long user) throws Exception {

		final String tokenCreatorMethod = "createAuthenticationToken";
		final Method method = BlinckTestUtil.getMethod(TokenAuthenticationService.class, tokenCreatorMethod);

		whiteListService.addUserToWhiteList(user);
		return method.invoke(userTokenAuthService, user.toString(), USER_ROLES).toString();
	}

	protected static String randomUserPath() {
		return USER_ENDPOINT + "/" + TestUtils.randomGaussNumberString();
	}
	protected static String randomAdminPath() {
		return ADMIN_ENDPOINT + "/" + TestUtils.randomGaussNumberString();
	}
	protected static String randomRootPath() {
		return ROOT_ENDPOINT + "/" + TestUtils.randomGaussNumberString();
	}
	protected static String randomPublicPath() {
		return PUBLIC_ENDPOINT + "/" + TestUtils.randomGaussNumberString();
	}
	protected static String randomProtectedPath() {
		return PROTECTED_ENDPOINT + "/" + TestUtils.randomGaussNumberString();
	}

}