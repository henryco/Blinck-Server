package net.henryco.blinckserver.mvc.service.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv.PrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.BioEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.MediaEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.PublicProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.UserNameEntity;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserBaseProfileService
		extends BlinckDaoProvider<UserCoreProfile, Long> {


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
	public Boolean updateBio(Long userId, BioEntity profile)
			throws RuntimeException {

		if (profile == null || profile.getUserName() == null) return false;

		UserCoreProfile core = getDao().getById(userId);

		String nickname = core.getPublicProfile().getBio().getUserName().getNickname();
		String name = profile.getUserName().getNickname();
		if (name.isEmpty()) name = null;

		if (name != null && getDao().isNickNameExists(name)) {
			if (nickname == null || !name.equals(nickname))
				throw new RuntimeException("Nickname already exists!");
		}


		core.getPublicProfile().setBio(profile);
		getDao().save(core);
		return true;
	}


	@Transactional
	public Boolean updateNickname(Long userId, String name) {

		if (name == null || name.isEmpty()) return false;

		UserCoreProfile core = getDao().getById(userId);
		if (core.getPublicProfile().getBio().getUserName().getNickname().equals(name))
			return true;

		if (getDao().isNickNameExists(name)) return false;

		core.getPublicProfile().getBio().getUserName().setNickname(name);
		getDao().save(core);
		return true;
	}


	@Transactional
	public Boolean updatePrivate(Long userId, PrivateProfile profile) {

		if (profile == null) return false;

		UserCoreProfile core = getDao().getById(userId);
		core.setPrivateProfile(profile);
		getDao().save(core);
		return true;
	}

}