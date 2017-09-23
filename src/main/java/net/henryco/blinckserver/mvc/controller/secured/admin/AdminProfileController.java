package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.security.token.service.SessionWhiteListService;
import net.henryco.blinckserver.security.credentials.AdminCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 24/08/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.ADMIN)
public class AdminProfileController
		implements BlinckProfileController, BlinckNotification {


	private final AdminDataService adminDataService;
	private final SessionWhiteListService whiteListService;
	private final UpdateNotificationService notificationService;

	@Autowired // TODO: 20/09/17 API LIST
	public AdminProfileController(AdminDataService adminDataService,
								  SessionWhiteListService whiteListService,
								  UpdateNotificationService notificationService) {
		this.adminDataService = adminDataService;
		this.whiteListService = whiteListService;
		this.notificationService = notificationService;
	}


	/*
	 *	Admin profile API
	 *
	 *		ENDPOINT: 		/protected/admin
	 *
	 *
	 * 	AdminCredentials:
	 *
	 * 		"user_id": 		CHAR[255],
	 * 		"password": 	CHAR[255]
	 *
	 *
	 *	SimpleNotification:
	 *
	 * 		"receiver_id": 	LONG,
	 * 		"type": 		CHAR[255],
	 * 		"notification": CHAR[255]
	 *
	 *
	 * 		REGISTRATION:
	 *
	 * 			ENDPOINT:	/registration
	 * 			METHOD:		POST
	 * 			BODY:		AdminCredentials
	 * 			RETURN:		VOID
	 *
	 *
	 * 		LIST:
	 *
	 * 			ENDPOINT:	/list
	 * 			METHOD:		GET
	 * 			RETURN:		STRING[]
	 *
	 *
	 * 		VERIFICATION:
	 *
	 * 			ENDPOINT:	/verification
	 * 			ARGS:		Int: size
	 * 			METHOD:		POST, GET
	 * 			RETURN:		STRING[]
	 *
	 *
	 * 		ACTIVATE:
	 *
	 * 			ENDPOINT:	/activate
	 * 			METHOD:		POST
	 * 			BODY:		STRING[]
	 * 			RETURN:		VOID
	 *
	 *
	 * 		AUTHORITY_ADD:
	 *
	 * 			ENDPOINT:	/authority/add
	 * 			ARGS:		String: name, role
	 * 			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 * 		AUTHORITY_REMOVE:
	 *
	 * 			ENDPOINT:	/authority/remove
	 * 			ARGS:		String: name, role
	 * 			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		SESSION_CLOSE_USER:
	 *
	 *			ENDPOINT:	/session/close/user
	 *			ARGS:		String: name
	 *			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 * 		SESSION_CLOSE_ADMIN:
	 *
	 *			ENDPOINT:	/session/close/admin
	 *			ARGS:		String: name
	 *			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		NOTIFICATION:
	 *
	 *			ENDPOINT:	/notification/user
	 *			METHOD:		POST
	 *			BODY:		SimpleNotification
	 *			RETURN:		VOID
	 *
	 */


	public @ResponseStatus(OK) @RequestMapping(
			method = POST,
			value = "/registration"
	) void registerAdmin(@RequestBody AdminCredentials credentials) {
		String user_id = credentials.getUser_id();
		String password = credentials.getPassword();
		adminDataService.addNewProfile(user_id, password);
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



	public @ResponseStatus(OK) @RequestMapping(
			method = POST,
			consumes = JSON,
			value = "/activate"
	) void activateAdminProfiles(@RequestBody String[] names,
								 Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		for (String name: names) {
			adminDataService.activateProfile(name);
		}
	}



	public @ResponseStatus(OK) @RequestMapping(
			method = {GET, POST},
			value = "/authority/add"
	) void grantAuthority(@RequestParam("name") String name,
						  @RequestParam("role") String role,
						  Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		adminDataService.addAuthority(name, role);
		whiteListService.removeAdminFromWhiteList(name);
	}



	public @ResponseStatus(OK) @RequestMapping(
			method = {GET, POST},
			value = "/authority/remove"
	) void removeAuthority(@RequestParam("name") String name,
						   @RequestParam("role") String role,
						   Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		adminDataService.removeAuthority(name, role);
		whiteListService.removeAdminFromWhiteList(name);
	}



	public @ResponseStatus(OK) @RequestMapping(
			method = {GET, POST},
			value = "/session/close/admin"
	) void logOutAdmin(@RequestParam("name") String target,
						Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		whiteListService.removeAdminFromWhiteList(target);
	}



	public @RequestMapping(
			method = {GET, POST},
			value = "/session/close/user"
	) void logOutUser(@RequestParam("name") Long target,
					   Authentication authentication) {
		rolesRequired(authentication, ROLE_MODERATOR);
		whiteListService.removeUserFromWhiteList(target);
	}



	/**
	 * <br>
	 * <h2>For JSON FORM, SEE: {@link SimpleNotification} </h2>
	 */
	public @ResponseStatus(OK) @RequestMapping(
			method = POST,
			value = "/notification/user",
			consumes = JSON
	) void sendNotificationToUser(@RequestBody SimpleNotification notification) {
		notificationService.addNotification(notification);
	}


}