package net.henryco.blinckserver.mvc.model.dao.security.imp;

import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.model.repository.security.UserAuthProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */
@Repository
public class UserAuthProfileDaoImp
		extends BlinckRepositoryProvider<UserAuthProfile, Long>
		implements UserAuthProfileDao {


	@Autowired
	public UserAuthProfileDaoImp(UserAuthProfileRepository profileRepository) {
		super(profileRepository, false);
	}

}
