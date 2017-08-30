package net.henryco.blinckserver.mvc.model.dao.profile.imp;

import net.henryco.blinckserver.mvc.model.dao.profile.UserNameEntityDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserNameEntity;
import net.henryco.blinckserver.mvc.model.repository.profile.UserNameProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */

@Repository
public class UserNameEntityDaoImp
		extends BlinckRepositoryProvider<UserNameEntity, Long>
		implements UserNameEntityDao {


	@Autowired
	public UserNameEntityDaoImp(UserNameProfileRepository nameProfileRepository) {
		super(nameProfileRepository, false);
	}


}