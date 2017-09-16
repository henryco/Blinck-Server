package net.henryco.blinckserver.mvc.controller.secured.user.group;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


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


@Component
final class MatcherServicePack {

	private static final String GENDER_FLUID_HELICOPTER
			= "Gender fluid helicopter McDonnell Douglas AH-64 Apache";

	protected final MatcherService matcherService;
	protected final UpdateNotificationService notificationService;
	protected final FriendshipService friendshipService;
	protected final UserBaseProfileService profileService;

	@Autowired
	MatcherServicePack(MatcherService matcherService,
					   UpdateNotificationService notificationService,
					   FriendshipService friendshipService,
					   UserBaseProfileService profileService) {
		this.matcherService = matcherService;
		this.notificationService = notificationService;
		this.friendshipService = friendshipService;
		this.profileService = profileService;
	}

	protected boolean genderFilter(Long userId, Type type) {

		final String gender = profileService.getGender(userId);
		final Type typo = Type.typeChecker(type);
		return typo.getIdent().equals(gender)
				|| typo.getWanted().equals(Type.BOTH)
				|| gender.equals(GENDER_FLUID_HELICOPTER);
	}
}


@RestController // TODO: 15/09/17 TESTS
@RequestMapping("/protected/user/match")
public class MatcherController
		implements BlinckController, BlinckNotification {

	private final TaskExecutors executors;
	private final MatcherServicePack servicePack;

	@Autowired
	public MatcherController(MatcherServicePack servicePack) {

		this.servicePack = servicePack;
		this.executors = new TaskExecutors();
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
	 *			RETURN:		VOID
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
	 *			RETURN:		VOID
	 *
	 *
	 *		CUSTOM_LEAVE:
	 *
	 *			ENDPOINT:	/queue/custom/leave
	 *			ARGS:		Long: id
	 *			METHOD:		POST, DELETE
	 *			RETURN:		VOID
	 *
	 *
	 *		CUSTOM_START:
	 *
	 *			ENDPOINT:	/queue/custom/start
	 *			ARGS:		Long: id
	 *			METHOD:		POST
	 *			RETURN: 	VOID
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
	 */
	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/solo",
			method = POST,
			consumes = JSON
	) void soloQueue(Authentication authentication,
					 @RequestBody Type type) {

		final Long id = longID(authentication);

		if (!servicePack.genderFilter(id, type)) return;
		if (getQueueList(authentication).length != 0) return;

		executors.subPartyTaskQueue.submit(() -> {

			final SubParty subParty = servicePack.matcherService.jointToExistingOrCreateSubParty(id, type);
			if (!subParty.getDetails().getInQueue()) {

				servicePack.notificationService.addNotification(id, TYPE.SUB_PARTY_IN_QUEUE, subParty.getId());
				executors.partyTaskQueue.submit(() -> findParty(subParty));
			}
		});
	}


	public @RequestMapping(
			value = "/queue/list",
			method = GET
	) Long[] getQueueList(Authentication authentication) {
		return Arrays.stream(servicePack.matcherService.getSubPartyWaitList(longID(authentication)))
				.map(SubParty::getId)
		.toArray(Long[]::new);
	}


	public @RequestMapping(
			value = "/queue/leave",
			method = {POST, DELETE}
	) void leaveQueue(Authentication authentication,
						 @RequestParam("id") Long subPartyId) {
		Long id = longID(authentication);

		executors.leaveTaskQueue.submit(() -> {
			boolean leaved = servicePack.matcherService.leaveSearchQueue(id, subPartyId);
			if (leaved) servicePack.notificationService.addNotification(id, TYPE.QUEUE_LEAVE, subPartyId);
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
	 */
	public @RequestMapping(
			value = "/queue/custom",
			method = POST,
			consumes = JSON
	) Long customQueue(Authentication authentication,
					   @RequestBody Type type) {

		final Long id = longID(authentication);
		if (!servicePack.genderFilter(id, type)) return null;
		SubPartyQueue custom = servicePack.matcherService.createCustomSubParty(id, type);
		return custom.getId();
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/custom/delete",
			method = DELETE
	) void deleteCustomQueue(Authentication authentication,
							 @RequestParam("id") Long customQueueId) {
		final Long id = longID(authentication);
		servicePack.matcherService.deleteCustomSubParty(id, customQueueId);
		servicePack.notificationService.addNotification(id, TYPE.CUSTOM_SUB_PARTY_REMOVE, customQueueId);
	}



	public @RequestMapping(
			value = "/queue/custom/list",
			method = GET
	) Long[] getCustomQueueList(Authentication authentication) {
		return Arrays.stream(servicePack.matcherService.getCustomSubPartyList(longID(authentication)))
				.map(SubPartyQueue::getId)
		.toArray(Long[]::new);
	}



	public @RequestMapping(
			value = "/queue/custom/join",
			method = POST
	) Boolean joinToCustomQueue(Authentication authentication,
							 @RequestParam("id") Long customQueueId) {

		final Long id = longID(authentication);
		SubPartyQueue queue = servicePack.matcherService.getCustomSubParty(customQueueId);

		if (queue.getOwner().equals(id)) return true;
		if (getQueueList(authentication).length != 0) return false;
		if (!servicePack.genderFilter(id, queue.getType())) return false;

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



	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/custom/invite",
			method = POST
	) void inviteToCustomQueue(Authentication authentication,
							   @RequestParam("id") Long customQueueId,
							   @RequestBody Long[] users) {

		final Long id = longID(authentication);

		SubPartyQueue queue = servicePack.matcherService.getCustomSubParty(customQueueId);
		if (!queue.getOwner().equals(id)) return;

		for (Long user: users) {
			if (servicePack.friendshipService.isExistsBetweenUsers(user, id)
					&& servicePack.genderFilter(user, queue.getType()))
				servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_INVITE, queue.getId());
		}
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/custom/leave",
			method = {POST, DELETE}
	) void leaveCustomQueue(Authentication authentication,
							@RequestParam("id") Long customQueueId) {

		final Long id = longID(authentication);
		SubPartyQueue queue = servicePack.matcherService.leaveCustomSubParty(id, customQueueId);
		for (Long user : queue.getUsers()) {
			servicePack.notificationService.addNotification(user, TYPE.CUSTOM_SUB_PARTY_LEAVE, id);
			if (queue.getUsers().isEmpty())
				deleteCustomQueue(authentication, customQueueId);
		}
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/custom/start",
			method = POST
	) void startCustomQueue(Authentication authentication,
							@RequestParam("id") Long customQueueId) {

		if (getQueueList(authentication).length != 0) return;

		final Long id = longID(authentication);
		if (Arrays.stream(servicePack.matcherService.getCustomSubPartyList(id)).anyMatch(subPartyQueue
				-> customQueueId.equals(subPartyQueue.getId()))) {

			SubParty subParty = servicePack.matcherService.startCustomSubParty(customQueueId);
			if (!subParty.getDetails().getInQueue()) {

				servicePack.notificationService.addNotification(id, TYPE.SUB_PARTY_IN_QUEUE, subParty.getId());
				servicePack.notificationService.addNotification(id, TYPE.CUSTOM_SUB_PARTY_REMOVE, customQueueId);

				executors.partyTaskQueue.submit(() -> findParty(subParty));
			}
		}
	}



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

}