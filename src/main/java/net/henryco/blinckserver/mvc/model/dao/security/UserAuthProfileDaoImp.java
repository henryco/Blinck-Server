package net.henryco.blinckserver.mvc.model.dao.security;

import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.model.repository.security.UserAuthProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Henry on 23/08/17.
 */
@Component
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
