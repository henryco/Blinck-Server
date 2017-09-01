package net.henryco.blinckserver.mvc.model.dao.security.imp;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.repository.security.AdminAuthProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 30/08/17.
 */
@Repository
public class AdminAuthProfileDaoImp
		extends BlinckRepositoryProvider<AdminAuthProfile, String>
		implements AdminAuthProfileDao {


	@Autowired
	public AdminAuthProfileDaoImp(AdminAuthProfileRepository repository) {
		super(repository);
	}

}