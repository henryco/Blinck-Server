package net.henryco.blinckserver.unit.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv.PrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.BioEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.UserNameEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.MediaEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.UserPhotoEntity;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.mvc.service.profile.UserImageMediaService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import org.junit.Test;

/**
 * This is not regular test class
 * i used it for checking jsons forms
 * @author Henry on 22/09/17.
 */
public class JsonsTest extends BlinckUnitTest{


	private static String write(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return object.getClass().getSimpleName() + ":\n\n"+ mapper.writeValueAsString(object) + "\n\n";
	}

	@Test
	public void bioEntity() throws Exception {

		BioEntity object = new BioEntity();
		object.setUserName(new UserNameEntity());

		System.out.println(write(object));
	}

	@Test
	public void mediaEntity() throws Exception {
		MediaEntity object = new MediaEntity();
		object.setPhoto(new UserPhotoEntity());

		System.out.println(write(object));
	}

	@Test
	public void nameDetails() throws Exception {
		UserBaseProfileService.NameDetails object = new UserBaseProfileService.NameDetails();
		object.setUser(new UserNameEntity());

		System.out.println(write(object));
	}

	@Test
	public void privateProfile() throws Exception {
		System.out.println(write(new PrivateProfile()));
	}

	@Test
	public void userImageInfoTest() throws Exception {
		System.out.println(write(new UserImageMediaService.UserImageInfo()));
	}

	@Test
	public void notificationTest() throws Exception {
		System.out.println(write(new BlinckNotification.JsonNotificationForm()));
	}

	@Test
	public void typeFormTest() throws Exception {
		System.out.println(write(new Type()));
	}

}