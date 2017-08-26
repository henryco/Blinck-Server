package net.henryco.blinckserver.integration.security;

import net.henryco.blinckserver.integration.security.help.JsonForm;
import net.henryco.blinckserver.integration.security.help.MockFacebookUser;
import net.henryco.blinckserver.mvc.service.action.UserDataService;
import net.henryco.blinckserver.mvc.service.security.UserTokenAuthService;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.utils.HTTPTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;

import static net.henryco.blinckserver.utils.HTTPTestUtils.randomNumberString;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 25/08/17.
 */

@RunWith(SpringRunner.class)
@PropertySource("classpath:/static/props/base.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndPointAccessTest {


	private static final String HEADER_ACCESS_TOKEN_NAME = "Authorization";
	private static final String LOGIN_ENDPOINT_ADMIN = "/login/admin";

	private static final String ROOT_ENDPOINT = "/";
	private static final String PUBLIC_ENDPOINT = "/public";
	private static final String PROTECTED_ENDPOINT = "/protected";

	private static final String ADMIN_ENDPOINT = "/protected/admin";
	private static final String USER_ENDPOINT = "/protected/user";


	private static String randomUserPath() {
		return USER_ENDPOINT + "/" + randomNumberString();
	}
	private static String randomAdminPath() {
		return ADMIN_ENDPOINT + "/" + randomNumberString();
	}
	private static String randomRootPath() {
		return ROOT_ENDPOINT + "/" + randomNumberString();
	}
	private static String randomPublicPath() {
		return PUBLIC_ENDPOINT + "/" + randomNumberString();
	}
	private static String randomProtectedPath() {
		return PROTECTED_ENDPOINT + "/" + randomNumberString();
	}



	private @LocalServerPort int port;
	private TestRestTemplate restTemplate;


	private @Autowired Environment environment;
	private @Autowired UserDataService userDataService;
	private @Autowired UserTokenAuthService userTokenAuthService;




	private ResponseEntity<String> fastGetRequest(String endPoint) {
		return restTemplate.exchange(
				new RequestEntity(
						GET, HTTPTestUtils.newURI(endPoint, port)
				), String.class
		);
	}


	private ResponseEntity<String> authorizedGetRequest(String endPoint, String authToken) {

		return restTemplate.exchange(
				RequestEntity.get(HTTPTestUtils.newURI(endPoint, port))
						.header(HEADER_ACCESS_TOKEN_NAME, authToken)
						.accept(MediaType.APPLICATION_JSON)
						.build(),
				String.class
		);
	}


	private ResponseEntity<String> fastPostRequest(String endPoint, JsonForm postForm) {
		return restTemplate.exchange(
				RequestEntity.post(HTTPTestUtils.newURI(endPoint, port))
						.accept(MediaType.APPLICATION_JSON)
						.body(postForm),
				String.class
		);
	}


	private String getForAdminAuthToken() {
		return fastPostRequest(
				LOGIN_ENDPOINT_ADMIN, new JsonForm.AdminLoginPost(
						environment.getProperty("security.default.admin.name"),
						environment.getProperty("security.default.admin.password")
				)
		).getHeaders().get(HEADER_ACCESS_TOKEN_NAME).get(0);
	}


	private String getForUserAuthToken() throws Exception {
		final String tokenCreatorMethod = "createAuthenticationToken";
		final String tokenOwnerName = MockFacebookUser.getInstance().getUser().getId();
		final Method method = TokenAuthenticationService.class.getDeclaredMethod(tokenCreatorMethod, String.class);
		method.setAccessible(true);
		return method.invoke(userTokenAuthService, tokenOwnerName).toString();
	}




	@Before
	public void setUp() {

		userDataService.addNewFacebookUserIfNotExist(MockFacebookUser.getInstance().getUser());
		restTemplate = new TestRestTemplate();
	}



	@Test
	public void testPublicAdminAuthorization() {
		assert getForAdminAuthToken() != null;
	}



	@Test
	public void testPublicUserAuthorization() throws Exception {
		assert getForUserAuthToken() != null;
	}



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



	@Test
	public void testRootEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomRootPath(), authorization).getStatusCode();
	}



	@Test
	public void testPublicEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN == authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode();
	}



	@Test
	public void testUserEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN == authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomUserPath(), authorization).getStatusCode();
	}



	@Test
	public void testAdminEndPointAuthorizedAsAdmin() {

		final String authorization = getForAdminAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomAdminPath(), authorization).getStatusCode();
	}



	@Test
	public void testRootEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(ROOT_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomRootPath(), authorization).getStatusCode();
	}



	@Test
	public void testPublicEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PUBLIC_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomPublicPath(), authorization).getStatusCode();
	}



	@Test
	public void testProtectedEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(PROTECTED_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomProtectedPath(), authorization).getStatusCode();
	}



	@Test
	public void testUserEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN != authorizedGetRequest(USER_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN != authorizedGetRequest(randomUserPath(), authorization).getStatusCode();
	}



	@Test
	public void testAdminEndPointAuthorizedAsUser() throws Exception {

		final String authorization = getForUserAuthToken();

		assert FORBIDDEN == authorizedGetRequest(ADMIN_ENDPOINT, authorization).getStatusCode();
		assert FORBIDDEN == authorizedGetRequest(randomAdminPath(), authorization).getStatusCode();
	}
}