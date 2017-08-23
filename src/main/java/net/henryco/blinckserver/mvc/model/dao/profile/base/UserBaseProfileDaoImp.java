package net.henryco.blinckserver.mvc.model.dao.profile.base;

import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import org.springframework.stereotype.Component;

/**
 * @author Henry on 23/08/17.
 */
@Component
public class UserBaseProfileDaoImp implements UserBaseProfileDao {


	@Override
	public UserBaseProfile getById(Long aLong) {
		return null;
	}

	@Override
	public void save(UserBaseProfile entity) {

	}

	@Override
	public void deleteById(Long aLong) {

	}
}