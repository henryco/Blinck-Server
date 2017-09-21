package net.henryco.blinckserver.mvc.service.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.UserPhotoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.USER_IMAGE_URL;

/**
 * @author Henry on 21/09/17.
 */
@Service
public class UserImageMediaService {

	private final UserCoreProfileDao profileDao;

	@Autowired
	public UserImageMediaService(UserCoreProfileDao profileDao) {
		this.profileDao = profileDao;
	}


	@Data @NoArgsConstructor
	@AllArgsConstructor
	public static final class UserImageInfo
			implements Serializable {

		private Integer position;
		private String image;
	}


	private UserPhotoEntity getImageEntity(Long userId) {

		return profileDao.getById(userId)
				.getPublicProfile()
				.getMedia()
		.getPhoto();
	}


	@Transactional
	public String getUserAvatarLink(Long userId) {
		return USER_IMAGE_URL + getImageEntity(userId).getAvatar();
	}


	@Transactional
	public UserImageInfo[] getUserImages(Long userId) {

		String[] photoArray = getImageEntity(userId).getPhotoArray();
		UserImageInfo[] info = new UserImageInfo[photoArray.length];

		for (int i = 0; i < photoArray.length; i++)
			info[i] = new UserImageInfo(i, USER_IMAGE_URL + photoArray[i]);
		return info;
	}


}