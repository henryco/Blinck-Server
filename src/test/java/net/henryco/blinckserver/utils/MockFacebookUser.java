package net.henryco.blinckserver.utils;

import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.social.facebook.api.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Henry on 26/08/17.
 */
@SuppressWarnings("UnusedReturnValue")
public final class MockFacebookUser {

	private static final String GENDER_FOR_TEST = "Gender fluid helicopter McDonnell Douglas AH-64 Apache";
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

	private final List<String> idList;

//	private final String appNamespace;
//	private final String permissions;
//	private final String appSecret;
//	private final String appId;

//	private FacebookTemplate clientFacebook;
//	private TestUser testUser;
	private User facebookUser;


	private MockFacebookUser(String appId, String appSecret, String appNamespace, String permissions) {

		this.idList = new ArrayList<>();
//		this.appNamespace = appNamespace;
//		this.permissions = permissions;
//		this.appSecret = appSecret;
//		this.appId = appId;

		createRandom();
	}



	public MockFacebookUser createRandom() {

//		OAuth2Operations oauth = new FacebookServiceProvider(appId, appSecret, appNamespace).getOAuthOperations();
//		AccessGrant clientGrant = oauth.authenticateClient();//
//		this.clientFacebook = new FacebookTemplate(clientGrant.getAccessToken(), appNamespace, appId);
//		this.testUser = wipeUser().createTestUser();

		this.facebookUser = new User(
				createNewRandomId(),
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString(),
				GENDER_FOR_TEST,
				Locale.getDefault()
		);

		String MM = TestUtils.randomNumberString(1, 12);
		String DD = TestUtils.randomNumberString(1, 26);
		String YYYY = TestUtils.randomNumberString(1990, 2000);

		try {
			Field birthDayField = User.class.getDeclaredField("birthday");
			birthDayField.setAccessible(true);
			birthDayField.set(facebookUser, MM + "/" + DD + "/" +YYYY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}


//	public MockFacebookUser wipeUser() {
//		if (testUser != null)
//			clientFacebook.testUserOperations().deleteTestUser(testUser.getId());
//		return this;
//	}
//
//
//	private TestUser createTestUser() {
//		return clientFacebook.testUserOperations().createTestUser(
//				true,
//				permissions,
//				"Blinck Test User"
//		);
//	}



	public User getUser() {
		return facebookUser;
	}

//	public String getAccessToken() {
//		if (testUser == null) return null;
//		return testUser.getAccessToken();
//	}


	@BlinckTestName("createNewRandomId")
	private String createNewRandomId() {

		while (true) {
			String id = TestUtils.randomGaussNumberString();
			if (!idList.contains(id)) {
				idList.add(id);
				return id;
			}
		}
	}
}