package net.henryco.blinckserver.mvc.controller.secured.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 26/08/17.
 */
@RestController @RequestMapping("/protected/user")
public class UserProfileController {

	public @RequestMapping(
			method = GET,
			value = "/profile"
	) String profile() {
		return "wellcome user";
	}

}