package net.henryco.blinckserver.mvc.service.profile;

import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplateProvider;
import net.henryco.blinckserver.util.entity.BlinckEntityRemovalForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 23/08/17.
 */
@Service @Transactional
public class UserAuthProfileService
		extends BlinckDaoProvider<UserAuthProfile, Long> {


	@Autowired
	public UserAuthProfileService(UserAuthProfileDao authProfileDao) {
		super(authProfileDao);
	}

	private UserAuthProfileDao getDao() {
		return provideDao();
	}

	@Override
	public void deleteById(Long id) {
		throw new BlinckEntityRemovalForbiddenException(UserAuthProfile.class);
	}

	@Transactional
	public void setUserLocked(Long userId, Boolean locked) {

		UserAuthProfile authProfile = getDao().getById(userId);
		authProfile.setLocked(locked);
		getDao().save(authProfile);
	}

}