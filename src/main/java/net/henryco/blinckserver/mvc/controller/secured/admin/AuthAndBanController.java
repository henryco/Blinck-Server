package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.service.infrastructure.ReportAndBanService;
import net.henryco.blinckserver.mvc.service.profile.UserAuthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
final class AuthAndBanServicePack {

	protected final ReportAndBanService reportAndBan;
	protected final UserAuthProfileService authProfile;

	@Autowired
	public AuthAndBanServicePack(ReportAndBanService reportAndBanService,
								 UserAuthProfileService authProfileService) {
		this.reportAndBan = reportAndBanService;
		this.authProfile = authProfileService;
	}
}


/**
 * @author Henry on 20/09/17.
 */
@RestController // TODO: 20/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.ADMIN_BANS)
public class AuthAndBanController
		implements BlinckController {

	private final AuthAndBanServicePack services;

	@Autowired
	public AuthAndBanController(AuthAndBanServicePack services) {
		this.services = services;
	}


	/*
	 *	Ban controller API
	 *
	 *		ENDPOINT: 		/protected/admin/bans
	 *
	 *
	 *	ReportList:
	 *
	 *		"id": 			LONG,
	 *		"reporter_id": 	LONG,
	 *		"reported_id": 	LONG,
	 *		"reason": 		CHAR[255]
	 *
	 *
	 *	UserAuthProfile:
	 *
	 *		"id": 			LONG,
	 *		"locked": 		BOOLEAN,
	 *		"authorities": 	CHAR[255]
	 *
	 *
	 * 		COUNT:
	 *
	 * 			ENDPOINT:	/count/user
	 * 			METHOD:		GET
	 * 			RETURN:		LONG
	 *
	 *
	 * 		LIST:
	 *
	 * 			ENDPOINT:	/list/user
	 * 			ARGS:		Int: page, size
	 * 			METHOD:		GET
	 * 			RETURN:		UserAuthProfile[]
	 *
	 *
	 * 		DETAILS:
	 *
	 * 			ENDPOINT:	/details/user
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		UserAuthProfile
	 *
	 *
	 * 		LOCK:
	 *
	 * 			ENDPOINT:	/lock/user
	 * 			ARGS:		Long: id, Boolean: option
	 * 			METHOD:		POST, GET
	 * 			RETURN:		VOID
	 *
	 *
	 * 		REPORTS_COUNT:
	 *
	 * 			ENDPOINT:	/reports/user/count
	 * 			METHOD:		GET
	 * 			RETURN:		LONG
	 *
	 *
	 * 		REPORTS_LIST_USER:
	 *
	 * 			ENDPOINT:	/reports/user/list
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 *			RETURN:		ReportList[]
	 *
	 *
	 *		REPORTS_LIST_ALL:
	 *
	 *			ENDPOINT:	/reports/user/list/all
	 *			ARGS:		Int: page, size
	 *			METHOD:		GET
	 *			RETURN:		ReportList[]
	 *
	 *
	 *		REPORTS_CLEAR:
	 *
	 *			ENDPOINT:	/reports/user/clear
	 *			ARGS:		Long: id
	 *			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 */


	public @RequestMapping(
			value = "/count/user",
			method = GET
	) Long countBannedProfiles() {
		return services.authProfile.countBannedProfiles();
	}


	public @RequestMapping(
			value = "/list/user",
			method = GET,
			produces = JSON
	) UserAuthProfile[] getBannedList(@RequestParam("page") int page,
									  @RequestParam("size") int size) {
		return services.authProfile.getLockedProfiles(page, size);
	}


	public @RequestMapping(
			value = "/details/user",
			method = GET,
			produces = JSON
	) UserAuthProfile getAuthProfile(@RequestParam("id") Long profileId) {
		return services.authProfile.getById(profileId);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/lock/user",
			method = {GET, POST}
	) void setUserLocked(@RequestParam("id") Long userId,
						 @RequestParam("option") Boolean locked) {
		services.authProfile.setUserLocked(userId, locked);
	}


	public @RequestMapping(
			value = "/reports/user/count",
			method = GET
	) Long countReportLists() {
		return services.reportAndBan.countReportLists();
	}


	public @RequestMapping(
			value = "/reports/user/list",
			method = GET,
			produces = JSON
	) ReportList[] getReportListForUser(@RequestParam("id") Long userId) {
		return services.reportAndBan.getReportListForUser(userId);
	}


	public @RequestMapping(
			value = "/reports/user/list/all",
			method = GET,
			produces = JSON
	) ReportList[] getReportListForAll(@RequestParam("page") int page,
									   @RequestParam("size") int size) {
		return services.reportAndBan.getReportList(page, size);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/reports/user/clear",
			method = {GET, POST}
	) void clearUserReports(@RequestParam("id") Long userId) {
		services.reportAndBan.deleteAllForUser(userId);
	}

}