package net.henryco.blinckserver.mvc.controller.pub;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 22/08/17.
 */
@RestController @RequestMapping("/public")
public class PublicInfoController {

	private static final String JSON = "application/json; charset=UTF-8";



	@RequestMapping(value = "/facebook/permissions", method = GET, produces = JSON)
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


}
