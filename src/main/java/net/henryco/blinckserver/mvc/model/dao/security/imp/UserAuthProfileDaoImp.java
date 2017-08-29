package net.henryco.blinckserver.mvc.model.dao.security.imp;

import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.model.repository.security.UserAuthProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */
@Repository
public class UserAuthProfileDaoImp implements UserAuthProfileDao {

	private final UserAuthProfileRepository profileRepository;

	@Autowired
	public UserAuthProfileDaoImp(UserAuthProfileRepository profileRepository) {
		this.profileRepository = profileRepository;

	}

	@Override
	public UserAuthProfile getById(Long id) {
		return profileRepository.getOne(id);
	}

	@Override
	public boolean isExists(Long id) {
		return profileRepository.exists(id);
	}

	@Override
	public UserAuthProfile save(UserAuthProfile entity) {
		return profileRepository.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		throw new RuntimeException("You cannot delete sub entity");
	}
}
