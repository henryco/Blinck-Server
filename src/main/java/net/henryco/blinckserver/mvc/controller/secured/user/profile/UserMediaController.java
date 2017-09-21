package net.henryco.blinckserver.mvc.controller.secured.user.profile;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.mvc.service.profile.UserImageMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Component
final class UserMediaServicePack {

	protected final UserBaseProfileService baseProfile;
	protected final UserImageMediaService media;

	@Autowired
	protected UserMediaServicePack(UserBaseProfileService baseProfile,
								   UserImageMediaService media) {
		this.baseProfile = baseProfile;
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
			value = "/list/image",
			method = GET,
			produces = JSON
	) void getUserImages(@RequestParam("id") Long id) {

	}



}