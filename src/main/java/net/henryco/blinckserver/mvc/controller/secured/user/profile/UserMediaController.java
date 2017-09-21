package net.henryco.blinckserver.mvc.controller.secured.user.profile;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Component
final class UserMediaServicePack {

	protected final UserBaseProfileService baseProfile;

	@Autowired
	protected UserMediaServicePack(UserBaseProfileService baseProfile) {
		this.baseProfile = baseProfile;
	}
}

@RestController // TODO: 21/09/17  TESTS
@RequestMapping(BlinckController.EndpointAPI.USER_MEDIA)
public class UserMediaController {

	private final UserMediaServicePack services;

	@Autowired
	public UserMediaController(UserMediaServicePack services) {
		this.services = services;
	}


}