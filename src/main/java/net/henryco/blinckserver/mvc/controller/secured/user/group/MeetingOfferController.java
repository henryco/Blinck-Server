package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import net.henryco.blinckserver.mvc.service.relation.queue.PartyMeetingOfferService;
import net.henryco.blinckserver.mvc.service.relation.queue.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static net.henryco.blinckserver.mvc.service.relation.queue.PartyMeetingOfferService.OfferInfo;

@Component
final class MeetingServicePack {

	protected final PartyService party;
	protected final UpdateNotificationService notification;
	protected final PartyMeetingOfferService meetingOffer;
	protected final VoteService vote;

	@Autowired
	public MeetingServicePack(PartyService partyService,
							  VoteService voteService,
							  UpdateNotificationService notificationService,
							  PartyMeetingOfferService meetingOfferService) {

		this.party = partyService;
		this.vote = voteService;
		this.notification = notificationService;
		this.meetingOffer = meetingOfferService;
	}

	protected void sendPartyNotification(Long partyId, String notificationType) {
		sendPartyMultiNotification(partyId, party.getAllUsersInParty(partyId), notificationType);
	}

	protected void sendPartyMultiNotification(Long partyId, Long[] users, String ... notificationTypes) {
		for (Long user: users)
			for (String notificationType: notificationTypes)
				notification.addNotification(user, notificationType, partyId);
	}
}


@RestController // TODO: 19/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.MEETING)
public class MeetingOfferController
		implements BlinckController, BlinckNotification {


	private static final String[] NOTIF_TYPE_ONE = {TYPE.PARTY_MEETING_VOTE};
	private static final String[] NOTIF_TYPE_TWO = {TYPE.PARTY_MEETING_VOTE, TYPE.PARTY_MEETING_VOTE_FINAL};

	private final MeetingServicePack service;

	@Autowired
	public MeetingOfferController(MeetingServicePack servicePack) {
		this.service = servicePack;
	}


	/*
	 *	Meeting offer API
	 *
	 *		ENDPOINT: 		/protected/user/group/meeting
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
	 * 		LIST:
	 *
	 * 			ENDPOINT:	/list
	 * 			ARGS:		Long: id
	 * 			METHOD:		GET
	 * 			RETURN:		OfferInfo[]
	 *
	 *
	 * 		PROPOSE:
	 *
	 * 			ENDPOINT:	/propose
	 * 			ARGS:		Long: id
	 * 			METHOD:		POST
	 * 			BODY:		Meeting
	 * 			RETURN:		Boolean
	 *
	 *
	 * 		VOTE:
	 *
	 * 			ENDPOINT:	/vote
	 * 			ARGS:		Long: proposition, Boolean: option
	 * 			METHOD:		POST, GET
	 * 			RETURN:		VOID
	 *
	 *
	 * 		VOTE_FINAL:
	 *
	 * 			ENDPOINT:	/vote/final
	 * 			ARGS:		Long: proposition, Boolean: option
	 * 			METHOD:		POST, GET
	 * 			RETURN:		VOID
	 *
	 */


	public @RequestMapping(
			value = "/list",
			method = GET,
			produces = JSON
	) OfferInfo[] getMeetingList(Authentication authentication,
								 @RequestParam("id") Long partyId) {

		final Long id = longID(authentication);
		final List<Long> users = service.party.getPartyInfo(partyId, id).getUsers();

		if (users.stream().noneMatch(id::equals)) return null;
		return service.meetingOffer.getOfferList(partyId);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/propose",
			method = POST,
			consumes = JSON
	) Boolean proposeMeeting(Authentication authentication,
							 @RequestParam("id") Long partyId,
							 @RequestBody Meeting proposition) {

		final Long id = longID(authentication);
		Boolean added = service.meetingOffer.addProposition(id, partyId, proposition);
		if (added) service.sendPartyNotification(partyId, TYPE.PARTY_MEETING_PROPOSITION);
		return added;
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/vote",
			method = {GET, POST}
	) void voteMeeting(Authentication authentication,
					   @RequestParam("proposition") Long propositionId,
					   @RequestParam("option") Boolean option) {

		final Long id = longID(authentication);
		final Long party = service.meetingOffer.getPartyId(propositionId);
		final Long[] users = service.party.getAllUsersInParty(party);

		if (option) {

			boolean limit = service.meetingOffer.voteForProposition(id, propositionId, users.length);
			if (limit) service.vote.deleteAllForTopic(party);
			service.sendPartyMultiNotification(party, users, limit ? NOTIF_TYPE_TWO : NOTIF_TYPE_ONE);
		} else {

			service.meetingOffer.voteAgainstProposition(id, propositionId);
			service.sendPartyMultiNotification(party, users, NOTIF_TYPE_ONE);
		}

	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/vote/final",
			method = {GET, POST}
	) void voteMeetingFinal(Authentication authentication,
							@RequestParam("proposition") Long propositionId,
							@RequestParam("option") Boolean option) {

		final Long id = longID(authentication);
		final Long party = service.meetingOffer.getPartyId(propositionId);
		final Long[] users = service.party.getAllUsersInParty(party);

		if (option) {

			service.vote.vote(party, id, true);
			if (service.vote.countForTopic(party) >= users.length) {

				Meeting meeting = service.meetingOffer.getOfferedMeetingById(propositionId);
				boolean activated = service.party.setMeeting(party, meeting);

				if (activated) {
					service.vote.deleteAllForTopic(party);
					service.sendPartyMultiNotification(party, users,
							TYPE.PARTY_MEETING_VOTE_FINAL_SUCCESS,
							TYPE.PARTY_MEETING_SET
					);
				}
			}

		} else {
			service.vote.deleteAllForTopic(party);
			service.sendPartyMultiNotification(party, users, TYPE.PARTY_MEETING_VOTE_FINAL_FAIL);
		}
	}

}