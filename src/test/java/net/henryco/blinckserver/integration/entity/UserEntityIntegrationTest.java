package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.profile.core.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;

/**
 * @author Henry on 03/09/17.
 */
public abstract class UserEntityIntegrationTest extends BlinckIntegrationTest {


	protected  @Autowired UserDataService userDataService;
	protected @Autowired UserCoreProfileDao coreProfileDao;


	protected static Long[]
	saveNewRandomUsers(UserEntityIntegrationTest context) {
		return saveNewRandomUsers(context, 2);
	}

	protected static Long[]
	saveNewRandomUsers(UserEntityIntegrationTest context, int numb) {

		Long[] ids = new Long[numb];
		for (int i = 0; i < numb; i++) {
			User user = MockFacebookUser.getInstance().createRandom().getUser();
			context.userDataService.addNewFacebookUser(user);
			ids[i] = Long.decode(user.getId());
		}
		return ids;
	}

}