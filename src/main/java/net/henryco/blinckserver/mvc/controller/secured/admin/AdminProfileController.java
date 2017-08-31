package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 24/08/17.
 */
@RestController
@RequestMapping("/protected/admin")
public class AdminProfileController implements BlinckProfileController {

	private static final String JSON = "application/json; charset=UTF-8";

	private final AdminDataService adminDataService;


	@Autowired
	public AdminProfileController(AdminDataService adminDataService) {
		this.adminDataService = adminDataService;
	}



	public @RequestMapping(
			method = POST,
			consumes = JSON,
			value = "/activate/admin/"
	) void activateAdminProfiles(@RequestBody String[] names) {
		for (String name: names) {
			adminDataService.activateProfile(name);
		}
	}


	public @RequestMapping(
			method = GET,
			produces = JSON,
			value = "/verification"
	) @Secured(
			{"ROLE_MODERATOR"}
	) String[] getAdminVerificationList(@RequestParam int n) {

		return adminDataService
				.getVerificationQueue(n)
				.stream()
				.map(AdminVerificationQueue::getAdminProfile)
		.toArray(String[]::new);
	}

}