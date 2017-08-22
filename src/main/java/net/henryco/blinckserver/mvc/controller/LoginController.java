package net.henryco.blinckserver.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 21/08/17.
 */
@RestController
@RequestMapping("/enter/facebook")
public class LoginController {


	@RequestMapping(value = "/permissions", method = GET, produces = "application/json; charset=UTF-8")
	public String[] getRequestedFbPermissions() {

		return new String[]{
				"user_about_me",
				"user_birthday",
				"user_education_history",
				"user_friends",
				"user_likes",
				"user_location",
				"user_photos",
				"read_custom_friendlists"
		};
	}

	@RequestMapping(method = GET)
	public String logIn(@RequestParam("token") String fbToken) {

		System.out.println(fbToken);
		return "";
	}

}
