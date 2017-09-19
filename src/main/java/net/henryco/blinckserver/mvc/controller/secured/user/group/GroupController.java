package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import net.henryco.blinckserver.mvc.service.relation.queue.PartyMeetingOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

import static net.henryco.blinckserver.mvc.service.relation.core.PartyService.PartyInfo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static net.henryco.blinckserver.mvc.service.relation.queue.PartyMeetingOfferService.OfferInfo;

@Component
final class GroupServicePack {

	protected final PartyService partyService;
	protected final UpdateNotificationService notificationService;
	protected final PartyMeetingOfferService meetingOfferService;

	@Autowired
	public GroupServicePack(PartyService partyService,
							UpdateNotificationService notificationService,
							PartyMeetingOfferService meetingOfferService) {

		this.partyService = partyService;
		this.notificationService = notificationService;
		this.meetingOfferService = meetingOfferService;
	}

	protected void sendPartyNotification(Long partyId, String notificationType) {
		for (Long user: partyService.getAllUsersInParty(partyId))
			notificationService.addNotification(user, notificationType, partyId);
	}

}

@RestController // TODO: 17/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.GROUP)
public class GroupController
		implements BlinckController, BlinckNotification {

	private final GroupServicePack servicePack;

	@Autowired
	public GroupController(GroupServicePack servicePack) {
		this.servicePack = servicePack;
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
	 *	OfferInfo:
	 *
	 *		todo
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
		return servicePack.partyService.getAllPartyInfoIdWithUser(longID(authentication));
	}


	public @RequestMapping(
			value = "/list/details",
			method = GET,
			produces = JSON
	) PartyInfo[] getPartyDetailsList(Authentication authentication) {
		return servicePack.partyService.getAllPartyInfoWithUser(longID(authentication));
	}


	public @RequestMapping(
			value = "/details",
			method = GET,
			produces = JSON
	) PartyInfo getPartyDetails(Authentication authentication,
								@RequestParam("id") Long partyId) {
		return servicePack.partyService.getPartyInfo(partyId, longID(authentication));
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


	public @RequestMapping(
			value = "/meeting/list",
			method = GET,
			produces = JSON
	) OfferInfo[] getMeetingList(Authentication authentication,
								 @RequestParam("id") Long partyId) {

		if (Arrays.stream(getPartyUsers(authentication, partyId))
				.noneMatch(longID(authentication)::equals)) return null;
		return servicePack.meetingOfferService.getOfferList(partyId);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/meeting/propose",
			method = POST,
			consumes = JSON
	) Boolean proposeMeeting(Authentication authentication,
						  @RequestParam("id") Long partyId,
						  @RequestBody Meeting proposition) {

		final Long id = longID(authentication);
		Boolean added = servicePack.meetingOfferService.addProposition(id, partyId, proposition);
		if (added) servicePack.sendPartyNotification(partyId, TYPE.PARTY_MEETING_PROPOSITION);
		return added;
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/meeting/vote",
			method = {GET, POST}
	) void voteMeeting(Authentication authentication,
					   @RequestParam("proposition") Long propositionId,
					   @RequestParam("option") Boolean option) {

		final Long id = longID(authentication);
		// TODO: 19/09/17
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/meeting/vote/final",
			method = {GET, POST}
	) void voteMeetingFinal(Authentication authentication,
					   @RequestParam("proposition") Long propositionId,
					   @RequestParam("option") Boolean option) {

		final Long id = longID(authentication);
		// TODO: 19/09/17
	}

}