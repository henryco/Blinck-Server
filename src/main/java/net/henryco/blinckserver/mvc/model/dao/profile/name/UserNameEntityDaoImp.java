package net.henryco.blinckserver.mvc.model.dao.profile.name;

import net.henryco.blinckserver.mvc.model.entity.profile.UserNameEntity;
import net.henryco.blinckserver.mvc.model.repository.profile.UserNameProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 23/08/17.
 */

@Repository
public class UserNameEntityDaoImp implements UserNameEntityDao {


	private final UserNameProfileRepository nameProfileRepository;

	@Autowired
	public UserNameEntityDaoImp(UserNameProfileRepository nameProfileRepository) {
		this.nameProfileRepository = nameProfileRepository;
	}


	@Override
	public UserNameEntity getById(Long id) {
		return nameProfileRepository.getOne(id);
	}

	@Override
	public boolean isExists(Long id) {
		return nameProfileRepository.exists(id);
	}

	@Override
	public UserNameEntity save(UserNameEntity entity) {
		return nameProfileRepository.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		throw new RuntimeException("You cannot delete sub entity");
	}
}