package net.henryco.blinckserver.mvc.model.dao.profile.imp;

import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.repository.profile.UserCoreProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */
@Repository
public class UserCoreProfileDaoImp
		extends BlinckRepositoryProvider<UserCoreProfile, Long>
		implements UserCoreProfileDao {


	@Autowired
	public UserCoreProfileDaoImp(UserCoreProfileRepository baseProfileRepository) {
		super(baseProfileRepository);
	}


}