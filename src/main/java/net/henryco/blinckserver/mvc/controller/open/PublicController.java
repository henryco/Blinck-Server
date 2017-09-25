package net.henryco.blinckserver.mvc.controller.open;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 22/08/17.
 */ @RestController @RequestMapping(BlinckController.EndpointAPI.PUBLIC)
@PropertySource("classpath:/static/props/base.properties")
public class PublicController {

	private static final String JSON = "application/json; charset=UTF-8";

	@Value("${app.info.name}") private String app_name;
	@Value("${app.info.about}") private String app_about;


	public @RequestMapping(
			method = GET
	) String main() {
		return this.about();
	}


	public @RequestMapping(
			value = "/about",
			method = GET
	) String about() {
		return app_name + "\n" + app_about;
	}


	public @RequestMapping(
			method = GET, produces = JSON,
			value = "/facebook/permissions"
	) String[] getRequestedFbPermissions() {
		return new String[] {
				"user_birthday",
				"user_location",
				"user_likes",
				"user_education_history",
				"user_photos",
				"user_friends",
				"user_about_me",
				"read_custom_friendlists",
				"public_profile"
		};
	}

}
