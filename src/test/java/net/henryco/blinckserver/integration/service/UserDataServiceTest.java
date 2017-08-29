package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;

/**
 * @author Henry on 29/08/17.
 */
public class UserDataServiceTest extends BlinckIntegrationTest {


	private @Autowired UserDataService userDataService;


	@Test
	public void addNewFbUserTest() {

		User user = MockFacebookUser.getInstance().createRandom().getUser();
		userDataService.addNewFacebookUser(user);
	}



}