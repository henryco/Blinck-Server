package net.henryco.blinckserver.mvc.model.dao.profile.pub.imp;

import net.henryco.blinckserver.mvc.model.dao.profile.pub.UserPublicProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.pub.UserPublicProfile;
import net.henryco.blinckserver.mvc.model.repository.profile.pub.UserPublicProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 30/08/17.
 */ @Repository
public class UserPublicProfileDaoImp
		extends BlinckRepositoryProvider<UserPublicProfile, Long>
		implements UserPublicProfileDao {

	@Autowired
	public UserPublicProfileDaoImp(UserPublicProfileRepository repository) {
		super(repository, false);
	}

}