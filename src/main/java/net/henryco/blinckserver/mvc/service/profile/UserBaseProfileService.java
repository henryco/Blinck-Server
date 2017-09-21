package net.henryco.blinckserver.mvc.service.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.*;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserBaseProfileService extends BlinckDaoProvider<UserCoreProfile, Long> {


	@Data @NoArgsConstructor
	@AllArgsConstructor
	public static final class NameDetails
			implements Serializable {

		private Long id;
		private UserNameEntity user;

		private NameDetails(UserCoreProfile coreProfile) {

			this.id = coreProfile.getId();
			this.user = coreProfile.getPublicProfile().getBio().getUserName();
		}
	}


	@Autowired
	public UserBaseProfileService(UserCoreProfileDao baseProfileDao) {
		super (((baseProfileDao))) ;
	}

	private UserCoreProfileDao getDao() {
		return provideDao();
	}



	@Transactional
	public String getGender(Long userId) {
		return getDao().getById(userId).getPublicProfile().getBio().getGender();
	}


	@Transactional
	public PublicProfile getPublicProfile(Long userId) {
		return new PublicProfile[]{getDao().getById(userId).getPublicProfile()}[0];
	}


	@Transactional
	public BioEntity getBio(Long userId) {
		return getPublicProfile(userId).getBio();
	}


	@Transactional
	public BioEntity getBio(String nickname) {
		return getDao().getByNickName(nickname).getPublicProfile().getBio();
	}


	@Transactional
	public NameDetails[] findByUserName(String username, int page, int size) {

		return getDao().findByUserName(username, page, size)
				.stream().map(NameDetails::new)
		.toArray(NameDetails[]::new);
	}


	@Transactional
	public PrivateProfile getPrivateProfile(Long userId) {
		return getDao().getById(userId).getPrivateProfile();
	}


	@Transactional
	public MediaEntity getMedia(Long userId) {
		return getDao().getById(userId).getPublicProfile().getMedia();
	}


	@Transactional
	public void updateBio(Long userId, BioEntity profile)
			throws RuntimeException {

		if (profile == null || profile.getUserName() == null) return;
		if (getDao().isNickNameExists(profile.getUserName().getNickname()))
			throw new RuntimeException("Nickname already exists!");

		UserCoreProfile core = getDao().getById(userId);
		core.getPublicProfile().setBio(profile);
		getDao().save(core);
	}


	@Transactional
	public void updateNickname(Long userId, String name) {

		if (name == null || name.isEmpty()) return;
		if (getDao().isNickNameExists(name))
			throw new RuntimeException("Nickname already exists!");

		UserCoreProfile core = getDao().getById(userId);
		core.getPublicProfile().getBio().getUserName().setNickname(name);
		getDao().save(core);
	}


	@Transactional
	public void updatePrivate(Long userId, PrivateProfile profile) {

		if (profile == null) return;

		UserCoreProfile core = getDao().getById(userId);
		core.setPrivateProfile(profile);
		getDao().save(core);
	}

}