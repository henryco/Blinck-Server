package net.henryco.blinckserver.integration.security.help;

import net.henryco.blinckserver.utils.HTTPTestUtils;
import org.springframework.social.facebook.api.TestUser;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookServiceProvider;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;

import java.util.Locale;

/**
 * @author Henry on 26/08/17.
 */
public final class MockFacebookUser {

	private static MockFacebookUser instance;

	public synchronized static MockFacebookUser newInstance(String appId, String appSecret, String permissions) {
		if (MockFacebookUser.instance == null)
			instance = new MockFacebookUser(appId, appSecret, permissions);
		return MockFacebookUser.instance;
	}
	public static MockFacebookUser newInstance(String appId, String appSecret) {
		return newInstance(appId, appSecret, "");
	}





	private final String permissions;
	private final String appSecret;
	private final String appId;

	private FacebookTemplate clientFacebook;
	private TestUser testUser;
	private User facebookUser;


	private MockFacebookUser(String appId, String appSecret, String permissions) {

		this.permissions = permissions;
		this.appSecret = appSecret;
		this.appId = appId;

		createRandom();
	}



	public MockFacebookUser createRandom() {

		OAuth2Operations oauth = new FacebookServiceProvider(appId, appSecret, null).getOAuthOperations();
		AccessGrant clientGrant = oauth.authenticateClient();

		this.clientFacebook = new FacebookTemplate(clientGrant.getAccessToken(), "", appId);
		this.testUser = createTestUser();

		this.facebookUser = new User(
				testUser.getId(),
				HTTPTestUtils.randomNumberString(),
				HTTPTestUtils.randomNumberString(),
				HTTPTestUtils.randomNumberString(),
				"Helicopter",
				Locale.getDefault()
		);
		return this;
	}



	private TestUser createTestUser() {

		if (testUser != null) {
			clientFacebook.testUserOperations().deleteTestUser(testUser.getId());
		}

		return clientFacebook.testUserOperations().createTestUser(
				true,
				permissions,
				"Blinck Test User"
		);
	}




	public User getUser() {
		return facebookUser;
	}

	public String getAccessToken() {
		if (testUser == null) return null;
		return testUser.getAccessToken();
	}
}