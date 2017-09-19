package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static net.henryco.blinckserver.mvc.service.relation.core.PartyService.PartyInfo;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
final class GroupServicePack {

	protected final PartyService party;
	protected final UpdateNotificationService notification;

	@Autowired
	public GroupServicePack(PartyService partyService,
							UpdateNotificationService notificationService) {
		this.party = partyService;
		this.notification = notificationService;
	}
}

@RestController // TODO: 17/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.GROUP)
public class GroupController
		implements BlinckController, BlinckNotification {

	private final GroupServicePack service;

	@Autowired
	public GroupController(GroupServicePack servicePack) {
		this.service = servicePack;
	}


	/*
	 *	Group API
	 *
	 *		ENDPOINT: 		/protected/user/group
	 *
	 *
	 *	PartyInfo:
	 *
	 * 		todo
	 *
	 *
	 *	Meeting:
	 *
	 *		"time": 		DATE / LONG,
	 *		"active_after": DATE / LONG,
	 *		"venue": 		CHAR[510]
	 *
	 *
	 *		DETAILS:
	 *
	 *			ENDPOINT:	/details
	 *			ARGS:		Long: id
	 *			METHOD:		GET
	 *			RETURN:		PartyInfo
	 *
	 *
	 * 		MEETING:
	 *
	 * 			ENDPOINT:	/details/meeting
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		Meeting
	 *
	 *
	 * 		USERS:
	 *
	 * 			ENDPOINT:	/details/users
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		Long[]
	 *
	 *
	 * 		SUB_GROUPS:
	 *
	 * 			ENDPOINT:	/details/subgroups
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		Long[]
	 *
	 *
	 * 		IS_ACTIVE:
	 *
	 * 			ENDPOINT:	/details/active
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		BOOLEAN
	 *
	 *
	 *		LIST_ID:
	 *
	 *			ENDPOINT:	/list/id
	 *			METHOD:		GET
	 *			RETURN:		Long[]
	 *
	 *
	 * 		LIST_DETAILS:
	 *
	 * 			ENDPOINT:	/list/details
	 * 			METHOD:		GET
	 * 			RETURN:		PartyInfo[]
	 *
	 */



	public @RequestMapping(
			value = "/list/id",
			method = GET,
			produces = JSON
	) Long[] getPartyIdList(Authentication authentication) {
		return service.party.getAllPartyInfoIdWithUser(longID(authentication));
	}


	public @RequestMapping(
			value = "/list/details",
			method = GET,
			produces = JSON
	) PartyInfo[] getPartyDetailsList(Authentication authentication) {
		return service.party.getAllPartyInfoWithUser(longID(authentication));
	}


	public @RequestMapping(
			value = "/details",
			method = GET,
			produces = JSON
	) PartyInfo getPartyDetails(Authentication authentication,
								@RequestParam("id") Long partyId) {
		return service.party.getPartyInfo(partyId, longID(authentication));
	}


	public @RequestMapping(
			value = "/details/meeting",
			method = GET,
			produces = JSON
	) Meeting getPartyMeeting(Authentication authentication,
							  @RequestParam("id") Long partyId) {
		return getPartyDetails(authentication, partyId).getMeeting();
	}


	public @RequestMapping(
			value = "/details/users",
			method = GET,
			produces = JSON
	) Long[] getPartyUsers(Authentication authentication,
						   @RequestParam("id") Long partyId) {
		return getPartyDetails(authentication, partyId).getUsers().toArray(new Long[0]);
	}


	public @RequestMapping(
			value = "/details/subgroups",
			method = GET,
			produces = JSON
	) Long[] getPartySubGroups(Authentication authentication,
							   @RequestParam("id") Long partyId) {
		return getPartyDetails(authentication, partyId).getSubParties().toArray(new Long[0]);
	}


	public @RequestMapping(
			value = "/details/active",
			method = GET
	) Boolean isPartyIsActive(Authentication authentication,
							  @RequestParam("id") Long partyId) {
		return getPartyMeeting(authentication, partyId)
				.getActivationTime().before(new Date(System.currentTimeMillis()));
	}

}