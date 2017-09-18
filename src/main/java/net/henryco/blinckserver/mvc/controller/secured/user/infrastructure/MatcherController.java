package net.henryco.blinckserver.mvc.controller.secured.user.infrastructure;
import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubPartyQueue;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.mvc.service.infrastructure.MatcherService;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static net.henryco.blinckserver.mvc.service.relation.core.SubPartyService.SubPartyInfo;

final class TaskExecutors {

	protected final ExecutorService notificationTaskQueue;
	protected final ExecutorService subPartyTaskQueue;
	protected final ExecutorService partyTaskQueue;
	protected final ExecutorService leaveTaskQueue;

	protected TaskExecutors() {

		this.notificationTaskQueue = Executors.newFixedThreadPool(10);
		this.subPartyTaskQueue = Executors.newSingleThreadExecutor();
		this.partyTaskQueue = Executors.newSingleThreadExecutor();
		this.leaveTaskQueue = Executors.newSingleThreadExecutor();
	}
}


/**
 *	Utility service provider class
 *	for internal user only.
 * */ @Component
final class MatcherServicePack {

	/**
	 * I sexually Identify as an Attack Helicopter.
	 * Ever since I was a boy I dreamed of
	 * soaring over the oilfields dropping hot sticky
	 * loads on disgusting foreigners. People say to me
	 * that a person being a helicopter is Impossible
	 * and I’m fucking retarded but I don’t care, I’m beautiful.
	 * I’m having a plastic surgeon install rotary blades,
	 * 30 mm cannons and AMG-114 Hellfire missiles on my body.
	 * From now on I want you guys to call me “Apache”
	 * and respect my right to kill from above and kill needlessly.
	 * If you can’t accept me you’re a heliphobe
	 * and need to check your vehicle privilege.
	 * Thank you for being so understanding.
	 * FIXME: 17/09/17
	 */ private static final String GENDER_FLUID_HELICOPTER
			= "Gender fluid helicopter McDonnell Douglas AH-64 Apache";

	protected final MatcherService matcherService;
	protected final UpdateNotificationService notificationService;
	protected final FriendshipService friendshipService;
	protected final UserBaseProfileService profileService;
	protected final SubPartyService subPartyService;

	@Autowired
	MatcherServicePack(MatcherService matcherService,
					   UpdateNotificationService notificationService,
					   FriendshipService friendshipService,
					   UserBaseProfileService profileService,
					   SubPartyService subPartyService) {

		this.matcherService = matcherService;
		this.notificationService = notificationService;
		this.friendshipService = friendshipService;
		this.profileService = profileService;
		this.subPartyService = subPartyService;
	}

	protected boolean checkYourGenderPrivilege(Long userId, Type type) {

		final String gender = profileService.getGender(userId);
		final Type typo = Type.typeChecker(type);
		return typo.getIdent().equalsIgnoreCase(gender)
				|| typo.getWanted().equalsIgnoreCase(Type.BOTH)
				|| gender.equals(GENDER_FLUID_HELICOPTER);
	}
}


@RestController
@RequestMapping(BlinckController.EndpointAPI.MATCHER)
public class MatcherController
		implements BlinckController, BlinckNotification {

	private final TaskExecutors executors;
	private final MatcherServicePack servicePack;
	private final Helper helper;

	@Autowired
	public MatcherController(MatcherServicePack servicePack) {

		this.servicePack = servicePack;
		this.executors = new TaskExecutors();
		this.helper = new Helper();
	}


	private final class Helper {

		private void
		findParty(SubParty subParty) {

			Party party = servicePack.matcherService.joinToExistingOrCreateParty(subParty);
			if (!party.getDetails().getInQueue())
				executors.notificationTaskQueue.submit(() -> createPartyNotification(party));
		}

		private void
		createPartyNotification(Party party) {

			for (Long subPartyId : party.getSubParties()) {

				Long[] members = servicePack.matcherService.getSubPartyMembers(subPartyId);
				for (Long userId : members) {

					servicePack.notificationService.addNotification(userId, TYPE.PARTY_FOUND, party.getId());
					servicePack.notificationService.addNotification(userId, TYPE.SUB_PARTY_FOUND, subPartyId);
				}
			}
		}

		private void
		leavePartyNotification(SubPartyInfo[] subParties) {

			for (SubPartyInfo info : subParties) {
				for (Long user : info.getUsers()) {
					servicePack.notificationService.addNotification(user, TYPE.QUEUE_LEAVE, info.getId());
				}
			}
		}

	}


	/*
	 *	Party matcher API
	 *
	 *		ENDPOINT: 		/protected/user/match
	 *
	 *
	 *	Type (queue) JSON:
	 *
	 * 		"ident": 		CHAR[255],
	 * 		"wanted": 		CHAR[255],
	 * 		"dimension": 	INTEGER
	 *
	 *
	 *		SOLO:
	 *
	 *			ENDPOINT:	/queue/solo
	 *			METHOD:		POST
	 *			BODY:		Type
	 *			RETURN:		VOID
	 *
	 *
	 *		LIST:
	 *
	 *			ENDPOINT:	/queue/list
	 *			METHOD:		GET
	 *			RETURN:		Long[]
	 *
	 *
	 * 		LEAVE:
	 *
	 * 			ENDPOINT:	/queue/leave
	 * 			ARGS:		Long: id
	 * 			METHOD:		POST, DELETE
	 * 			RETURN:		VOID
	 *
	 *
	 *		CUSTOM:
	 *
	 *			ENDPOINT:	/queue/custom
	 *			METHOD:		POST
	 *			BODY:		Type
	 *			RETURN:		Long
	 *
	 *
	 *		CUSTOM_DELETE:
	 *
	 *			ENDPOINT:	/queue/custom/delete
	 *			ARGS:		Long: id
	 *			METHOD:		DELETE
	 *			RETURN:		BOOLEAN
	 *
	 *
	 *		CUSTOM_LIST:
	 *
	 *			ENDPOINT:	/queue/custom/list
	 *			METHOD:		GET
	 *			RETURN:		Long[]
	 *
	 *
	 *		CUSTOM_JOIN:
	 *
	 *			ENDPOINT:	/queue/custom/join
	 *			ARGS:		Long: id
	 *			METHOD:		POST
	 *			RETURN:		BOOLEAN
	 *
	 *
	 *		CUSTOM_INVITE:
	 *
	 *			ENDPOINT:	/queue/custom/invite
	 *			ARGS:		Long: id
	 *			METHOD:		POST
	 *			BODY:		Long[]
	 *			RETURN:		BOOLEAN
	 *
	 *
	 *		CUSTOM_LEAVE:
	 *
	 *			ENDPOINT:	/queue/custom/leave
	 *			ARGS:		Long: id
	 *			METHOD:		POST, DELETE
	 *			RETURN:		BOOLEAN
	 *
	 *
	 *		CUSTOM_START:
	 *
	 *			ENDPOINT:	/queue/custom/start
	 *			ARGS:		Long: id
	 *			METHOD:		POST
	 *			RETURN: 	BOOLEAN
	 *
	 */



	/**
	 * <h1>Income Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "ident":			CHAR[255], &nbsp;
	 *         "wanted":		CHAR[255], &nbsp;
	 *         "dimension":		INTEGER
	 *     &nbsp;}
	 * </h2>
	 *
	 * @see Type
	 */ // Tested
	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/solo",
			method = POST,
			consumes = JSON
	) void soloQueue(Authentication authentication,
					 @RequestBody Type type) {

		final Long id = longID(authentication);

		if (!servicePack.checkYourGenderPrivilege(id, type)) return;
		if (getQueueList(authentication).length != 0) return;

		executors.subPartyTaskQueue.submit(() -> {

			final SubParty subParty = servicePack.matcherService.jointToExistingOrCreateSubParty(id, type);
			if (!subParty.getDetails().getInQueue()) {

				servicePack.notificationService.addNotification(id, TYPE.SUB_PARTY_IN_QUEUE, subParty.getId());
				executors.partyTaskQueue.submit(() -> helper.findParty(subParty));
			}
		});
	}



	// Tested
	public @RequestMapping(
			value = "/queue/list",
			method = GET
	) Long[] getQueueList(Authentication authentication) {
		return Arrays.stream(servicePack.matcherService.getSubPartyWaitList(longID(authentication)))
				.map(SubParty::getId)
		.toArray(Long[]::new);
	}



	// Tested
	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/leave",
			method = {POST, DELETE}
	) void leaveQueue(Authentication authentication,
					  @RequestParam("id") Long subPartyId) {

		Long id = longID(authentication);

		executors.leaveTaskQueue.submit(() -> {
			SubPartyInfo[] subParties = servicePack.subPartyService.getRelatedSubParties(subPartyId);
			boolean leaved = servicePack.matcherService.leaveSearchQueue(id, subPartyId);
			if (leaved) helper.leavePartyNotification(subParties);
		});
	}



	/**
	 * <h1>Income Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "ident":			CHAR[255], &nbsp;
	 *         "wanted":		CHAR[255], &nbsp;
	 *         "dimension":		INTEGER
	 *     &nbsp;}
	 * </h2>
	 *
	 * @see Type
	 */ // Tested
	public @RequestMapping(
			value = "/queue/custom",
			method = POST,
			consumes = JSON
	) Long customQueue(Authentication authentication,
					   @RequestBody Type type) {

		final Long id = longID(authentication);
		if (!servicePack.checkYourGenderPrivilege(id, type)) return null;
		SubPartyQueue custom = servicePack.matcherService.createCustomSubParty(id, type);
		return custom.getId();
	}



	// Tested
	public @RequestMapping(
			value = "/queue/custom/delete",
			method = DELETE
	) Boolean deleteCustomQueue(Authentication authentication,
								@RequestParam("id") Long customQueueId) {
		final Long id = longID(authentication);
		List<Long> users = servicePack.matcherService.getCustomSubParty(customQueueId).getUsers();

		Boolean deleted = servicePack.matcherService.deleteCustomSubParty(id, customQueueId);
		if (deleted) {
			for (Long user : users)
				servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_REMOVE, customQueueId);
		}
		return deleted;
	}



	// Tested
	public @RequestMapping(
			value = "/queue/custom/list",
			method = GET
	) Long[] getCustomQueueList(Authentication authentication) {
		return Arrays.stream(servicePack.matcherService.getCustomSubPartyList(longID(authentication)))
				.map(SubPartyQueue::getId)
		.toArray(Long[]::new);
	}



	// Tested
	public @RequestMapping(
			value = "/queue/custom/join",
			method = POST
	) Boolean joinToCustomQueue(Authentication authentication,
							 @RequestParam("id") Long customQueueId) {

		final Long id = longID(authentication);
		SubPartyQueue queue = servicePack.matcherService.getCustomSubParty(customQueueId);

		if (queue.getOwner().equals(id)) return true;
		if (getQueueList(authentication).length != 0) return false;
		if (!servicePack.checkYourGenderPrivilege(id, queue.getType())) return false;

		if (servicePack.friendshipService.isExistsBetweenUsers(id, queue.getOwner())) {

			boolean added = servicePack.matcherService.addUserToCustomSubParty(id, customQueueId);
			if (added) {
				for (Long user : queue.getUsers())
					servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_JOIN, id);
				return true;
			}
		}
		return false;
	}



	public @RequestMapping(
			value = "/queue/custom/invite",
			method = POST
	) Boolean inviteToCustomQueue(Authentication authentication,
							   @RequestParam("id") Long customQueueId,
							   @RequestBody Long[] users) {

		final Long id = longID(authentication);

		SubPartyQueue queue = servicePack.matcherService.getCustomSubParty(customQueueId);
		if (!queue.getOwner().equals(id)) return false;

		for (Long user: users) {
			if (servicePack.friendshipService.isExistsBetweenUsers(user, id)
					&& servicePack.checkYourGenderPrivilege(user, queue.getType()))
				servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_INVITE, queue.getId());
		}
		return true;
	}



	// Tested
	public @RequestMapping(
			value = "/queue/custom/leave",
			method = {POST, DELETE}
	) Boolean leaveCustomQueue(Authentication authentication,
							   @RequestParam("id") Long customQueueId) {

		final Long id = longID(authentication);
		SubPartyQueue queue = servicePack.matcherService.leaveCustomSubParty(id, customQueueId);
		if (queue.getOwner().equals(id)) return deleteCustomQueue(authentication, customQueueId);

		for (Long user : queue.getUsers()) {
			servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_LEAVE, id);
			if (queue.getUsers().isEmpty())
				return deleteCustomQueue(authentication, customQueueId);
		}
		return true;
	}



	// Tested
	public @RequestMapping(
			value = "/queue/custom/start",
			method = POST
	) Boolean startCustomQueue(Authentication authentication,
							@RequestParam("id") Long customQueueId) {

		if (getQueueList(authentication).length != 0) return false;

		final Long id = longID(authentication);
		if (Arrays.stream(servicePack.matcherService.getCustomSubPartyList(id)).anyMatch(subPartyQueue
				-> customQueueId.equals(subPartyQueue.getId()))) {

			SubParty subParty = servicePack.matcherService.startCustomSubParty(customQueueId);
			if (!subParty.getDetails().getInQueue()) {

				servicePack.notificationService.addNotification(id, TYPE.SUB_PARTY_IN_QUEUE, subParty.getId());
				servicePack.notificationService.addNotification(id, TYPE.CUSTOM_SUB_PARTY_REMOVE, customQueueId);

				executors.partyTaskQueue.submit(() -> helper.findParty(subParty));
				return true;
			}
		}
		return false;
	}

}