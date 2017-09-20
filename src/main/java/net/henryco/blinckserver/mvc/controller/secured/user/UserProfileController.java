package net.henryco.blinckserver.mvc.controller.secured.user;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Component
final class UserProfileServicePack {

	// TODO: 21/09/17
}

/**
 * @author Henry on 26/08/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.USER)
public class UserProfileController implements BlinckProfileController {

	private final UserProfileServicePack services;

	@Autowired
	public UserProfileController(UserProfileServicePack services) {
		this.services = services;
	}



}