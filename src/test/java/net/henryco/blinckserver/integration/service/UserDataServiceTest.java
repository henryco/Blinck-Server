package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.utils.MockFacebookUser;
import net.henryco.blinckserver.utils.TestedLoop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;

import java.util.Random;

/**
 * @author Henry on 29/08/17.
 */
public class UserDataServiceTest extends BlinckIntegrationTest {


	private static final TestedLoop testLoop = new TestedLoop(100);

	private @Autowired UserDataService userDataService;
	private @Autowired UserBaseProfileService baseProfileService;



	@Test
	public void addAndDeleteUserTest() throws Exception {

		testLoop.test(() -> {
			User user = MockFacebookUser.getInstance().createRandom().getUser();

			if (new Random().nextGaussian() > 0)
				userDataService.addNewFacebookUser(user);
			else userDataService.addNewAdminUser(user);

			assert baseProfileService.isExists(Long.decode(user.getId()));
			userDataService.deleteUser(user.getId());

			assert !baseProfileService.isExists(Long.decode(user.getId()));
		});
	}



}