package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.profile.base.UserBaseProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserBaseProfileService {

	private final UserBaseProfileDao baseProfileDao;

	@Autowired
	public UserBaseProfileService(UserBaseProfileDao baseProfileDao) {
		this.baseProfileDao = baseProfileDao;
	}

	public UserBaseProfile save(UserBaseProfile profile) {
		return baseProfileDao.save(profile);
	}

	public boolean isUserExists(Long id) {
		return baseProfileDao.isExists(id);
	}


}