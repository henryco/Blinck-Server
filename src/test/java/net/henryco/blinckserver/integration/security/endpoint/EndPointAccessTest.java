package net.henryco.blinckserver.integration.security.endpoint;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.service.action.UserDataService;
import net.henryco.blinckserver.mvc.service.security.UserTokenAuthService;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.HTTPTestUtils;
import net.henryco.blinckserver.utils.JsonForm;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;

/**
 * @author Henry on 25/08/17.
 */

@PropertySource("classpath:/static/props/base.properties")
public abstract class EndPointAccessTest extends BlinckIntegrationTest {


	static final String HEADER_ACCESS_TOKEN_NAME = "Authorization";
	static final String LOGIN_ENDPOINT_ADMIN = "/login/admin";
	static final String ROOT_ENDPOINT = "/";
	static final String PUBLIC_ENDPOINT = "/public";
	static final String PROTECTED_ENDPOINT = "/protected";
	static final String ADMIN_ENDPOINT = "/protected/admin";
	static final String USER_ENDPOINT = "/protected/user";


	private @Autowired UserDataService userDataService;
	private @Autowired UserTokenAuthService userTokenAuthService;



	@Before
	public void setUp() {
		userDataService.addNewFacebookUserIfNotExist(MockFacebookUser.getInstance().getUser());
	}


	protected ResponseEntity<String> authorizedGetRequest(String endPoint, String authToken) {

		return restTemplate.exchange(
				RequestEntity.get(HTTPTestUtils.newURI(endPoint, port))
						.header(HEADER_ACCESS_TOKEN_NAME, authToken)
						.accept(MediaType.APPLICATION_JSON)
						.build(),
				String.class
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

		final String tokenCreatorMethod = "createAuthenticationToken";
		final String tokenOwnerName = MockFacebookUser.getInstance().getUser().getId();
		final Method method = BlinckTestUtil.getMethod(TokenAuthenticationService.class, tokenCreatorMethod);

		return method.invoke(userTokenAuthService, tokenOwnerName).toString();
	}




	static String randomUserPath() {
		return USER_ENDPOINT + "/" + randomNumberString();
	}
	static String randomAdminPath() {
		return ADMIN_ENDPOINT + "/" + randomNumberString();
	}
	static String randomRootPath() {
		return ROOT_ENDPOINT + "/" + randomNumberString();
	}
	static String randomPublicPath() {
		return PUBLIC_ENDPOINT + "/" + randomNumberString();
	}
	static String randomProtectedPath() {
		return PROTECTED_ENDPOINT + "/" + randomNumberString();
	}

}