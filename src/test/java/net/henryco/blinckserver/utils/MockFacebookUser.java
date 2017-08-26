package net.henryco.blinckserver.utils;

import org.springframework.social.facebook.api.TestUser;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import java.util.Locale;

/**
 * @author Henry on 26/08/17.
 */
@SuppressWarnings("UnusedReturnValue")
public final class MockFacebookUser {

	private static MockFacebookUser instance;

	public synchronized static MockFacebookUser getInstance(String appId,
															String appSecret,
															String appNamespace,
															String permissions) {
		if (MockFacebookUser.instance == null)
			instance = new MockFacebookUser(appId, appSecret, appNamespace, permissions);
		return MockFacebookUser.instance;
	}

	public static MockFacebookUser getInstance(String appId, String appSecret, String appNamespace) {
		return getInstance(appId, appSecret, appNamespace, "");
	}

	public static MockFacebookUser getInstance() {
		return getInstance("", "", "", "");
	}



	private final String appNamespace;
	private final String permissions;
	private final String appSecret;
	private final String appId;

	private FacebookTemplate clientFacebook;
	private TestUser testUser;
	private User facebookUser;


	private MockFacebookUser(String appId, String appSecret, String appNamespace, String permissions) {

		this.appNamespace = appNamespace;
		this.permissions = permissions;
		this.appSecret = appSecret;
		this.appId = appId;

		createRandom();
	}



	public MockFacebookUser createRandom() {

//		OAuth2Operations oauth = new FacebookServiceProvider(appId, appSecret, appNamespace).getOAuthOperations();
//		AccessGrant clientGrant = oauth.authenticateClient();//
//		this.clientFacebook = new FacebookTemplate(clientGrant.getAccessToken(), appNamespace, appId);
//		this.testUser = wipeUser().createTestUser();

		this.facebookUser = new User(
				HTTPTestUtils.randomNumberString(),
				HTTPTestUtils.randomNumberString(),
				HTTPTestUtils.randomNumberString(),
				HTTPTestUtils.randomNumberString(),
				"Helicopter",
				Locale.getDefault()
		);
		return this;
	}


	public MockFacebookUser wipeUser() {
		if (testUser != null)
			clientFacebook.testUserOperations().deleteTestUser(testUser.getId());
		return this;
	}


	private TestUser createTestUser() {
		return clientFacebook.testUserOperations().createTestUser(
				true,
				permissions,
				"Blinck Test User"
		);
	}



	public User getUser() {
		return facebookUser;
	}

//	public String getAccessToken() {
//		if (testUser == null) return null;
//		return testUser.getAccessToken();
//	}


}