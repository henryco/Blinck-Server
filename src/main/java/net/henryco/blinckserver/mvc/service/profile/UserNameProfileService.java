package net.henryco.blinckserver.mvc.service.profile;

import net.henryco.blinckserver.mvc.model.dao.profile.pub.UserNameEntityDao;
import net.henryco.blinckserver.mvc.model.entity.profile.pub.UserNameEntity;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import net.henryco.blinckserver.util.entity.BlinckEntityRemovalForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 29/08/17.
 */
@Service
public class UserNameProfileService extends BlinckDaoProvider<UserNameEntity, Long> {


	@Autowired
	public UserNameProfileService(UserNameEntityDao nameEntityDao) {
		super(nameEntityDao);
	}

	@Override
	public void deleteById(Long id) {
		throw new BlinckEntityRemovalForbiddenException(UserNameEntity.class);
	}

}