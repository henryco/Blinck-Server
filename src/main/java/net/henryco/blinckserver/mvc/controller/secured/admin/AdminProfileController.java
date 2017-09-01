package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.mvc.service.security.SessionWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private final SessionWhiteListService whiteListService;

	@Autowired
	public AdminProfileController(AdminDataService adminDataService,
								  SessionWhiteListService whiteListService) {
		this.adminDataService = adminDataService;
		this.whiteListService = whiteListService;
	}


	public @RequestMapping(
			method = GET,
			produces = JSON,
			value = "/list"
	) String[] getAdminProfileList() {
		return adminDataService.getAdminProfiles()
				.stream()
				.map(AdminAuthProfile::getId)
		.toArray(String[]::new);
	}



	public @RequestMapping(
			method = GET,
			produces = JSON,
			value = "/verification"
	) String[] getAdminVerificationList(@RequestParam("size") int n) {
		return adminDataService
				.getVerificationQueue(n)
				.stream()
				.map(AdminVerificationQueue::getId)
		.toArray(String[]::new);
	}



	public @RequestMapping(
			method = POST,
			consumes = JSON,
			value = "/activate/admin"
	) void activateAdminProfiles(@RequestBody String[] names,
								 Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		for (String name: names) {
			adminDataService.activateProfile(name);
		}
	}



	public @RequestMapping(
			method = POST,
			value = "/authority/add"
	) void grantAuthority(@RequestParam("name") String name,
						  @RequestParam("role") String role,
						  Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		adminDataService.addAuthority(name, role);
		whiteListService.removeAdminFromWhiteList(name);
	}



	public @RequestMapping(
			method = POST,
			value = "/authority/remove"
	) void removeAuthority(@RequestParam("name") String name,
						   @RequestParam("role") String role,
						   Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		adminDataService.removeAuthority(name, role);
		whiteListService.removeAdminFromWhiteList(name);
	}



	public @RequestMapping(
			method = {GET, POST},
			value = "/session/logout"
	) void logOut(Authentication authentication) {
		whiteListService.removeAdminFromWhiteList(authentication.getName());
	}



	public @RequestMapping(
			method = POST,
			value = "/session/logout/admin"
	) void logOutAdmin(@RequestParam("name") String target,
						Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		whiteListService.removeAdminFromWhiteList(target);
	}



	public @RequestMapping(
			method = POST,
			value = "/session/logout/user"
	) void logOutUser(@RequestParam("name") Long target,
					   Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		whiteListService.removeUserFromWhiteList(target);
	}



}