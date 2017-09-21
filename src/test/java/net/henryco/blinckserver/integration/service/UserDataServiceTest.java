package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.mvc.service.profile.UserAuthProfileService;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.MockFacebookUser;
import net.henryco.blinckserver.utils.TestUtils;
import net.henryco.blinckserver.utils.TestedLoop;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Henry on 29/08/17.
 */
public class UserDataServiceTest extends BlinckIntegrationTest {


	private @Autowired UserDataService userDataService;
	private @Autowired UserBaseProfileService baseProfileService;
	private @Autowired UserAuthProfileService authProfileService;



	@Test
	public void parseFacebookDateTest() throws Exception {

		final String birthday = "06/26/1995";
		Method parseFacebookDate = BlinckTestUtil.getMethod(
				UserDataService.class,
				"Helper.parseFacebookDate"
		);
		Object date = parseFacebookDate.invoke(null, birthday);
		DateTime dateTime = new DateTime(date);

		assert dateTime.getYear() == 1995;
		assert dateTime.getMonthOfYear() == 6;
		assert dateTime.getDayOfMonth() == 26;
	}


	@Test
	public void saveAndDeleteUserEntityTest() throws Exception {

		new TestedLoop(100).test(() -> {
			User user = MockFacebookUser.getInstance().createRandom().getUser();

			userDataService.addNewFacebookUser(user);

			assert baseProfileService.isExists(Long.decode(user.getId()));
			userDataService.deleteUser(user.getId());

			assert !baseProfileService.isExists(Long.decode(user.getId()));
			assert !authProfileService.isExists(Long.decode(user.getId()));
		});
	}


	@Test @Transactional
	public void saveUserEntityInDataBaseIdentityTest() throws Exception {

		final User user = MockFacebookUser.getInstance().createRandom().getUser();
		final Long id = Long.decode(user.getId());
		final String[] authorities = {UserDataService.ROLE_USER};

		final Method create = getEntityCreatorMethod();

		userDataService.addNewFacebookUser(user);

		UserCoreProfile baseProfile = (UserCoreProfile) create.invoke(null, null, user, authorities);
		UserCoreProfile profile = baseProfileService.getById(id);

		assert profile.equals(baseProfile);
	}


	@Test
	public void userEntityCreationTest() throws Exception {

		final User user = MockFacebookUser.getInstance().createRandom().getUser();
		final Method create = getEntityCreatorMethod();
		final String[] authorities = {
				TestUtils.randomGaussNumberString(),
				TestUtils.randomGaussNumberString()
		};

		UserCoreProfile baseProfile = (UserCoreProfile) create.invoke(null, null, user, authorities);

		profileIdAssertion(baseProfile);
		profileAuthAssertion(baseProfile.getAuthProfile());

		assert Arrays.equals(baseProfile.getAuthProfile().getAuthorityArray(), authorities);
	}


	private static Method
	getEntityCreatorMethod() throws Exception {

		return BlinckTestUtil.getMethod(
				UserDataService.class,
				"Helper.createUserEntity"
		);
	}

	private static void
	profileAuthAssertion(UserAuthProfile authProfile) {
		assert authProfile != null;
		assert authProfile.isEnabled();
		assert !authProfile.isExpired();
		assert !authProfile.isLocked();
		assert authProfile.getPassword() == null;
	}

	private static void
	profileIdAssertion(UserCoreProfile coreProfile) {
		assert coreProfile != null;
		assert coreProfile.getId().equals(coreProfile.getAuthProfile().getId());
	}

}