package net.henryco.blinckserver.integration;

import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;

/**
 * @author Henry on 03/09/17.
 */
public abstract class BlinckUserIntegrationTest extends BlinckIntegrationAccessTest {

	protected @Autowired UserCoreProfileDao coreProfileDao;

	protected static Long[]
	saveNewRandomUsers(BlinckUserIntegrationTest context) {
		return saveNewRandomUsers(context, 2);
	}

	protected static Long[]
	saveNewRandomUsers(BlinckUserIntegrationTest context, int numb) {

		Long[] ids = new Long[numb];
		for (int i = 0; i < numb; i++) {
			User user = MockFacebookUser.getInstance().createRandom().getUser();
			context.userDataService.addNewFacebookUser(user);
			ids[i] = Long.decode(user.getId());
		}
		return ids;
	}

}