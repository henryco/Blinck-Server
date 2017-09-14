package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.PrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.UserNameEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.PublicProfile;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserDataService {

	public static final String ROLE_USER = "ROLE_USER";
	public static final String FB_DATE_FORMAT = "MM/dd/yyyy";

	private final UserCoreProfileDao baseProfileDao;

	@Autowired
	public UserDataService(UserCoreProfileDao baseProfileDao) {
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
		UserCoreProfile user = createNewUser(userProfile, ROLE_USER);
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


	private UserCoreProfile createNewUser(User user, String ... authorities) {

		final Long id = Long.decode(user.getId());
		if (baseProfileDao.isExists(id))
			throw new RuntimeException("User: ["+id+"] already exists!");
		return createUserEntity(user, authorities);
	}




	private static @BlinckTestName("createUserEntity")
	UserCoreProfile createUserEntity(User user, String ... authorities) {

		final Long id = Long.decode(user.getId());

		UserCoreProfile userCoreProfile = new UserCoreProfile();
		userCoreProfile.setId(id);
		userCoreProfile.setPrivateProfile(createUserPrivateProfile(user));
		userCoreProfile.setPublicProfile(createUserPublicProfile(user));
		userCoreProfile.setAuthProfile(createUserAuthProfile(id, authorities));

		return userCoreProfile;
	}


	private static PrivateProfile createUserPrivateProfile(User user) {

		PrivateProfile userPrivateProfile = new PrivateProfile();
		userPrivateProfile.setEmail(user.getEmail());
		return userPrivateProfile;
	}


	private static
	UserAuthProfile createUserAuthProfile(Long id, String ... authorities){

		UserAuthProfile userAuthProfile = new UserAuthProfile();
		userAuthProfile.setId(id);
		userAuthProfile.setLocked(false);
		userAuthProfile.setAuthorityArray(authorities);
		return userAuthProfile;
	}


	private static
	UserNameEntity createUserNameEntity(User user) {

		UserNameEntity userNameEntity = new UserNameEntity();
		userNameEntity.setFirstName(user.getFirstName());
		userNameEntity.setSecondName(user.getMiddleName());
		userNameEntity.setLastName(user.getLastName());
		return userNameEntity;
	}


	private static PublicProfile createUserPublicProfile(User user) {

		PublicProfile publicProfile = new PublicProfile();
		publicProfile.setGender(user.getGender());
		publicProfile.setAbout(user.getAbout());
		publicProfile.setBirthday(parseFacebookDate(user.getBirthday()));
		publicProfile.setUserName(createUserNameEntity(user));
		return publicProfile;
	}


	private static
	@BlinckTestName("parseFacebookDate")
	Date parseFacebookDate(String date) {
		try {
			return new SimpleDateFormat(FB_DATE_FORMAT).parse(date);
		} catch (ParseException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}


}