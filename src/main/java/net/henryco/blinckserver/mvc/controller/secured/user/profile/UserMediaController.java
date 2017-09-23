package net.henryco.blinckserver.mvc.controller.secured.user.profile;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.profile.UserImageMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static net.henryco.blinckserver.mvc.service.profile.UserImageMediaService.UserImageInfo;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
final class UserMediaServicePack {

	protected final UserImageMediaService media;

	@Autowired
	protected UserMediaServicePack(UserImageMediaService media) {
		this.media = media;
	}
}

@RestController // TODO: 21/09/17  TESTS
@RequestMapping(BlinckController.EndpointAPI.USER_MEDIA)
public class UserMediaController implements BlinckController {

	private final UserMediaServicePack services;

	@Autowired
	public UserMediaController(UserMediaServicePack services) {
		this.services = services;
	}



	public @RequestMapping(
			value = "/image/max",
			method = GET
	) Integer getMaxUserImages() {
		return UserImageMediaService.MAX_IMAGES;
	}


	public @RequestMapping(
			value = "/image/list",
			method = GET,
			produces = JSON
	) UserImageInfo[] getUserImages(@RequestParam("id") Long id) {
		return services.media.getUserImages(id);
	}


	public @RequestMapping(
			value = "/image/avatar",
			method = GET,
			produces = JSON
	) String getUserAvatar(@RequestParam("id") Long id) {
		return services.media.getUserAvatarLink(id);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/image/swap",
			method = {POST, GET}
	) void swapUserImages(Authentication authentication,
						  @RequestParam("one") int one,
						  @RequestParam("two") int two) {
		services.media.swapImages(longID(authentication), one, two);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/image/delete",
			method = {DELETE, POST}
	) void deleteUserImage(Authentication authentication,
						   @RequestParam("image") int number) {
		services.media.deleteImage(longID(authentication), number);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/image/add",
			method = POST
	) void addUserImage(Authentication authentication,
						@RequestParam("image") MultipartFile image) throws IOException {
		services.media.addImage(longID(authentication), image.getBytes());
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/image/set",
			method = POST
	) void setUserImage(Authentication authentication,
						@RequestParam("image") MultipartFile image,
						@RequestParam("index") int index) throws IOException {
		services.media.setImage(longID(authentication), index, image.getBytes());
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/image/avatar",
			method = POST
	) void setUserAvatar(Authentication authentication,
						 @RequestParam("image") MultipartFile image) throws IOException {
		services.media.setAvatar(longID(authentication), image.getBytes());
	}


}