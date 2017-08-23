package net.henryco.blinckserver.mvc.model.dao.security;

import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.model.repository.UserAuthProfileRepository;
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
	public void save(UserAuthProfile entity) {
		profileRepository.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		profileRepository.delete(id);
	}
}
