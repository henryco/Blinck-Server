package net.henryco.blinckserver.mvc.controller.secured.user;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.BioEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.MediaEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv.PrivateProfile;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService.NameDetails;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
final class UserProfileServicePack {

	protected final UserBaseProfileService baseProfile;

	@Autowired
	protected UserProfileServicePack(UserBaseProfileService baseProfile) {
		this.baseProfile = baseProfile;
	}
}

/**
 * @author Henry on 26/08/17.
 */ @RestController // todo API DOC, TESTS
@RequestMapping(BlinckController.EndpointAPI.USER)
public class UserProfileController implements BlinckProfileController {

	private final UserProfileServicePack services;

	@Autowired
	public UserProfileController(UserProfileServicePack services) {
		this.services = services;
	}


	public @RequestMapping(
			value = "/profile/bio",
			method = GET,
			produces = JSON
	) BioEntity getBio(@RequestParam("id") Long userId) {
		return services.baseProfile.getBio(userId);
	}


	public @RequestMapping(
			value = "/profile/media",
			method = GET,
			produces = JSON
	) MediaEntity getMedia(@RequestParam("id") Long userId) {
		return services.baseProfile.getMedia(userId);
	}


	public @RequestMapping(
			value = "/profile/priv",
			method = GET,
			produces = JSON
	) PrivateProfile getPrivateProfile(Authentication authentication) {
		return services.baseProfile.getPrivateProfile(longID(authentication));
	}


	public @RequestMapping(
			value = "/profile",
			method = GET,
			produces = JSON
	) BioEntity getProfile(Authentication authentication) {
		return getBio(longID(authentication));
	}


	public @RequestMapping(
			value = "/find/one",
			method = GET,
			produces = JSON
	) BioEntity findByName(@RequestParam("username") String username) {
		return services.baseProfile.getBio(username);
	}


	public @RequestMapping(
			value = "/find",
			method = GET,
			produces = JSON
	) NameDetails[] findByName(@RequestParam("username") String username,
							   @RequestParam("page") int page,
							   @RequestParam("size") int size) {
		return services.baseProfile.findByUserName(username, page, size);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/profile/update/bio",
			method = POST,
			consumes = JSON
	) void updateBio(Authentication authentication,
					 @RequestBody BioEntity bio) {
		services.baseProfile.updateBio(longID(authentication), bio);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/profile/update/nickname",
			method = POST,
			consumes = JSON
	) void updateNickname(Authentication authentication,
						  @RequestBody String name) {
		services.baseProfile.updateNickname(longID(authentication), name);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/profile/update/priv",
			method = POST,
			consumes = JSON
	) void updatePrivateProfile(Authentication authentication,
								@RequestBody PrivateProfile profile) {
		services.baseProfile.updatePrivate(longID(authentication), profile);
	}


}