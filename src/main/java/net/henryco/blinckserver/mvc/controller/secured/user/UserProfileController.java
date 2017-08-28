package net.henryco.blinckserver.mvc.controller.secured.user;

import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 26/08/17.
 */
@RestController
@RequestMapping("/protected/user")
public class UserProfileController implements BlinckProfileController {



	@Override
	public String profile() {
		return "wellcome user";
	}



}