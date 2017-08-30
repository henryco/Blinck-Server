package net.henryco.blinckserver.mvc.model.dao.profile.imp;

import net.henryco.blinckserver.mvc.model.dao.profile.UserBaseProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import net.henryco.blinckserver.mvc.model.repository.profile.UserBaseProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */
@Repository
public class UserBaseProfileDaoImp
		extends BlinckRepositoryProvider<UserBaseProfile, Long>
		implements UserBaseProfileDao {


	@Autowired
	public UserBaseProfileDaoImp(UserBaseProfileRepository baseProfileRepository) {
		super(baseProfileRepository);
	}


}