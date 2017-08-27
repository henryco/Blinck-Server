package net.henryco.blinckserver.mvc.controller.secured.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 24/08/17.
 */
@RestController @RequestMapping("/protected/admin")
public class AdminProfileController {

	public @RequestMapping(
			method = GET,
			value = "/profile"
	) String profile() {
		return "wellcome admin";
	}
}