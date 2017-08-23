package net.henryco.blinckserver.mvc.model.dao.profile.base;

import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import net.henryco.blinckserver.mvc.model.repository.profile.UserBaseProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Henry on 23/08/17.
 */
@Component
public class UserBaseProfileDaoImp implements UserBaseProfileDao {

	private final UserBaseProfileRepository baseProfileRepository;


	@Autowired
	public UserBaseProfileDaoImp(UserBaseProfileRepository baseProfileRepository) {
		this.baseProfileRepository = baseProfileRepository;
	}



	@Override
	public UserBaseProfile getById(Long id) {
		return baseProfileRepository.getOne(id);
	}

	@Override
	public boolean isExists(Long id) {
		return baseProfileRepository.exists(id);
	}

	@Override
	public UserBaseProfile save(UserBaseProfile entity) {
		return baseProfileRepository.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		baseProfileRepository.delete(id);
	}
}