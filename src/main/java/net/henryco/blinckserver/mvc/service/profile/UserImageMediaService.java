package net.henryco.blinckserver.mvc.service.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.UserPhotoEntity;
import net.henryco.blinckserver.util.Utils;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Serializable;
import java.util.function.Consumer;

import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.REL_FILE_PATH;
import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.USER_IMAGE_POSTFIX;
import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.USER_IMAGE_URL;
import static net.henryco.blinckserver.mvc.service.profile.UserImageMediaService.ArrayHelper.*;

/**
 * @author Henry on 21/09/17.
 */
@Service
public class UserImageMediaService {

	public static final int MAX_IMAGES = 3;
	public static final String STORE_PATH = REL_FILE_PATH + USER_IMAGE_POSTFIX;

	private final UserCoreProfileDao profileDao;

	@Autowired
	public UserImageMediaService(UserCoreProfileDao profileDao) {
		this.profileDao = profileDao;
	}


	@BlinckTestName
	protected static abstract class ArrayHelper {


		protected static @BlinckTestName
		String[] delete(int index, final String ... array) {

			if (index < 0 || array.length <= index) return array;

			String[] newArray = new String[array.length - 1];
			int k = 0;

			for (int i = 0; i < array.length; i++) {
				if (i == index)
					continue;
				newArray[k] = array[i];
				k++;
			}

			return newArray;
		}


		protected static @BlinckTestName
		String[] put(int index, String element, final String ... array) {

			if (index < 0) return array;

			int position = Math.min(index, array.length);

			String[] newArray = new String[array.length + 1];
			int k = 0;

			for (int i = 0; i < newArray.length; i++) {

				if (i == position) {
					newArray[i] = element;
					continue;
				}

				newArray[i] = array[k];
				k++;
			}

			return newArray;
		}


		protected static @BlinckTestName
		String[] swap(int from, int to, final String ... array) {

			if (from < 0 || from == to ||  to < 0)
				return array;
			if (from >= array.length || to >= array.length)
				return array;

			String[] newArray = new String[array.length];
			String one = array[from];
			String two = array[to];

			System.arraycopy(array, 0, newArray, 0, array.length);
			newArray[from] = two;
			newArray[to] = one;

			return newArray;
		}


		protected static @BlinckTestName
		String[] add(String element, final String ... array) {
			return put(array.length, element, array);
		}

	}


	@Data @NoArgsConstructor
	@AllArgsConstructor
	public static final class UserImageInfo
			implements Serializable {

		private Integer position;
		private String image;
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


	@Transactional
	public void deleteImage(Long userId, int index) {

		photoConsumer(userId, photo -> {

			String[] photoArray = photo.getPhotoArray();
			if (index > 0 && index < photoArray.length)
				tryToRemove(photoArray[index]);

			photo.setPhotoArray(delete(index, photoArray));
		});
	}


	@Transactional
	public void setAvatar(Long userId, byte[] image) {

		photoConsumer(userId, photo -> {

			String file = Utils.saveImageFile(image, userId.toString(), STORE_PATH);
			if (file == null || file.trim().isEmpty())
				return;

			tryToRemove(photo.getAvatar());

			photo.setAvatar(file);
		});
	}


	@Transactional
	public void addImage(Long userId, byte[] image) {

		photoConsumer(userId, photo -> {

			String[] photoArray = photo.getPhotoArray();
			if (photoArray.length >= MAX_IMAGES)
				return;

			String file = Utils.saveImageFile(image, userId.toString(), STORE_PATH);
			if (file == null || file.trim().isEmpty())
				return;

			photo.setPhotoArray(add(file, photoArray));
		});
	}


	@Transactional
	public void setImage(Long userId, int index, byte[] image) {

		photoConsumer(userId, photo -> {

			String[] photoArray = photo.getPhotoArray();

			String file = Utils.saveImageFile(image, userId.toString(), STORE_PATH);
			if (file == null || file.trim().isEmpty())
				return;

			if (index > 0 && index < photoArray.length)
				tryToRemove(photoArray[index]);

			photo.setPhotoArray(put(index, file, photoArray));
		});
	}


	@Transactional
	public void swapImages(Long userId, int one, int two) {
		photoConsumer(userId, photo -> photo.setPhotoArray(swap(one, two, photo.getPhotoArray())));
	}




	private void
	photoConsumer(Long userId, Consumer<UserPhotoEntity> photoConsumer) {

		UserCoreProfile profile = profileDao.getById(userId);
		UserPhotoEntity photo = profile.getPublicProfile().getMedia().getPhoto();
		photoConsumer.accept(photo);
		profileDao.save(profile);
	}

	private UserPhotoEntity
	getImageEntity(Long userId) {
		return profileDao.getById(userId)
				.getPublicProfile()
				.getMedia()
		.getPhoto();
	}


	private static
	@SuppressWarnings("ResultOfMethodCallIgnored")
	void tryToRemove(String image) {

		try {
			File file = new File(STORE_PATH + image);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}