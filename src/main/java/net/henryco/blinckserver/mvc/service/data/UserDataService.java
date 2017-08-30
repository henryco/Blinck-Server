package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.profile.UserBaseProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.UserNameEntity;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserDataService {

	public static final String ROLE_USER = "ROLE_USER";

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
		UserBaseProfile user = createNewUser(userProfile, ROLE_USER);
		baseProfileDao.save(user);
	}


	@Transactional
	public void deleteUser(long id) {
		if (baseProfileDao.isExists(id))
			baseProfileDao.deleteById(id);
	}


	@Transactional
	public void deleteUser(String id) {
		deleteUser(Long.decode(id));
	}



	private UserBaseProfile createNewUser(User user, String ... authorities) {

		final Long id = Long.decode(user.getId());
		if (baseProfileDao.isExists(id))
			throw new RuntimeException("User: ["+id+"] already exists!");
		return createUserEntity(user, authorities);
	}



	@BlinckTestName("createUserEntity")
	private static UserBaseProfile createUserEntity(User user, String ... authorities) {

		final Long id = Long.decode(user.getId());

		UserNameEntity userNameEntity = new UserNameEntity();
		userNameEntity.setId(id);
		userNameEntity.setFirstName(user.getFirstName());
		userNameEntity.setSecondName(user.getMiddleName());
		userNameEntity.setLastName(user.getLastName());

		UserAuthProfile userAuthProfile = new UserAuthProfile();
		userAuthProfile.setId(id);
		userAuthProfile.setExpired(false);
		userAuthProfile.setLocked(false);
		userAuthProfile.setAuthorityArray(authorities);

		UserBaseProfile userBaseProfile = new UserBaseProfile();
		userBaseProfile.setId(id);
		userBaseProfile.setBirthday(user.getBirthday());
		userBaseProfile.setEmail(user.getEmail());
		userBaseProfile.setAbout(user.getAbout());
		userBaseProfile.setUserName(userNameEntity);
		userBaseProfile.setAuthProfile(userAuthProfile);

		return userBaseProfile;
	}

}