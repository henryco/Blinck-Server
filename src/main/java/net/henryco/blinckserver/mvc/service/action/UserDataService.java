package net.henryco.blinckserver.mvc.service.action;

import net.henryco.blinckserver.mvc.model.dao.profile.base.UserBaseProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.UserNameEntity;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserDataService {

	private final static String ROLE_USER = "ROLE_USER";

	private final UserBaseProfileDao baseProfileDao;

	@Autowired
	public UserDataService(UserBaseProfileDao baseProfileDao) {
		this.baseProfileDao = baseProfileDao;
	}




	@Transactional
	public void addNewFacebookUserIfNotExist(User userProfile) {
		final Long id = Long.decode(userProfile.getId());
		if (baseProfileDao.isExists(id)) return;
		addNewFacebookUser(userProfile);
	}


	@Transactional
	public void addNewFacebookUser(User userProfile) {

		final Long id = Long.decode(userProfile.getId());
		if (baseProfileDao.isExists(id))
			throw new RuntimeException("User: ["+id+"] already exists!");

		UserNameEntity userNameEntity = new UserNameEntity();
		userNameEntity.setId(id);
		userNameEntity.setFirstName(userProfile.getFirstName());
		userNameEntity.setSecondName(userProfile.getMiddleName());
		userNameEntity.setLastName(userProfile.getLastName());

		UserAuthProfile userAuthProfile = new UserAuthProfile();
		userAuthProfile.setId(id);
		userAuthProfile.setEnabled(true);
		userAuthProfile.setExpired(false);
		userAuthProfile.setLocked(false);
		userAuthProfile.setAuthorityArray(ROLE_USER);

		UserBaseProfile userBaseProfile = new UserBaseProfile();
		userBaseProfile.setId(id);
		userBaseProfile.setBirthday(userProfile.getBirthday());
		userBaseProfile.setEmail(userProfile.getEmail());
		userBaseProfile.setAbout(userProfile.getAbout());
		userBaseProfile.setUserName(userNameEntity);
		userBaseProfile.setAuthProfile(userAuthProfile);

		baseProfileDao.save(userBaseProfile);
	}


	public void deleteUser(long id) {
		if (baseProfileDao.isExists(id))
			baseProfileDao.deleteById(id);
	}

	public void deleteUser(String id) {
		deleteUser(Long.decode(id));
	}
}