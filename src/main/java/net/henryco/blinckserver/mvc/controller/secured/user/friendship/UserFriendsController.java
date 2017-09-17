package net.henryco.blinckserver.mvc.controller.secured.user.friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.FriendshipNotification;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.mvc.service.relation.queue.FriendshipNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 04/09/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.FRIENDSHIP)
public class UserFriendsController implements BlinckController, BlinckNotification.TYPE {


	private final FriendshipNotificationService friendNotificationService;
	private final UpdateNotificationService updateNotificationService;
	private final FriendshipService friendshipService;


	@Autowired
	public UserFriendsController(FriendshipNotificationService friendNotificationService,
								 UpdateNotificationService updateNotificationService,
								 FriendshipService friendshipService) {

		this.friendNotificationService = friendNotificationService;
		this.updateNotificationService = updateNotificationService;
		this.friendshipService = friendshipService;
	}



	@Data @NoArgsConstructor @AllArgsConstructor
	private static final class DetailedFriendship
			implements Serializable {
		private Long friendship;
		private Long friend;
	}


	/*
	 *	Friends controller API
	 *
	 *		ENDPOINT: 		/protected/user/friends
	 *
	 *
	 *	DetailedFriendship:
	 *
	 *		"friendship": 	LONG,
	 *		"friend": 		LONG,
	 *
	 *
	 * 	Friendship:
	 *
	 * 		"friendship": 	LONG,
	 * 		"timestamp": 	DATE/LONG,
	 * 		"user_1": 		LONG,
	 * 		"user_2": 		LONG
	 *
	 *
	 *	FriendshipNotification:
	 *
	 *		"notification": LONG,
	 *		"from": 		LONG,
	 *		"to": 			LONG,
	 *		"timestamp": 	DATE/LONG
	 *
	 *
	 *		COUNT:
	 *
	 *			ENDPOINT:	/count
	 *			METHOD:		GET
	 *			RETURN:		Long
	 *
	 *
	 *		LIST:
	 *
	 *			ENDPOINT:	/list
	 *			ARGS:		Int: page, Int: size
	 *			METHOD:		GET
	 *			RETURN:		Long[]
	 *
	 *
	 * 		DETAILED_LIST:
	 *
	 * 			ENDPOINT:	/detailed/list
	 * 			ARGS:		Int: page, Int: size
	 * 			METHOD:		GET
	 * 			RETURN:		DetailedFriendship[]
	 *
	 *
	 *		DETAILS:
	 *
	 *			ENDPOINT:	/Friendship
	 *			ARGS:		Int: id
	 *			METHOD:		GET
	 *			RETURN:		Friendship
	 *
	 *
	 *		ADD:
	 *
	 *			ENDPOINT:	/add
	 *			ARGS:		Long: user_id
	 *			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		REMOVE:
	 *
	 *			ENDPOINT:	/remove
	 *			ARGS:		Long: user_id
	 *			METHOD:		DELETE, POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		ACCEPT:
	 *
	 *			ENDPOINT:	/request/accept
	 *			ARGS:		Long: user_id
	 *			METHOD:		POST, GET
	 *			RETURN: 	VOID
	 *
	 *
	 *		DECLINE:
	 *
	 *			ENDPOINT:	/request/decline
	 *			ARGS:		Long: user_id
	 *			METHOD:		POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		DIRECT_REMOVE:
	 *
	 *			ENDPOINT:	/request/direct/delete
	 *			ARGS:		Long: id
	 *			METHOD:		DELETE, POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 * 		INCOME:
	 *
	 * 			ENDPOINT:	/request/list/income
	 * 			ARGS:		Int: page, Int: size
	 * 			METHOD:		GET
	 * 			RETURN:		FriendshipNotification[]
	 *
	 *
	 * 		OUTCOME:
	 *
	 * 			ENDPOINT:	/request/list/outcome
	 * 			ARGS:		Int: page, Int: size
	 * 			METHOD:		GET
	 * 			RETURN:		FriendshipNotification[]
	 *
	 */



	public @RequestMapping(
			value = "/count",
			method = GET
	) Long getFriendsCount(Authentication authentication) {
		return friendshipService.getFriendsCount(getID(authentication.getName()));
	}



	public @RequestMapping(
			value = "/list",
			method = GET,
			produces = JSON
	) Long[] getFriendList(Authentication authentication,
						   @RequestParam("page") int page,
						   @RequestParam("size") int size) {

		final Long id = getID(authentication.getName());
		BiFunction<Long, Long, Long> chooser = (a, b) -> a.equals(id) ? b : a;

		return friendshipService
				.getAllUserRelations(id, page, size)
				.stream().map(f -> chooser.apply(f.getUser1(), f.getUser2()))
		.toArray(Long[]::new);
	}



	public @RequestMapping(
			value = "/detailed/list",
			method = GET,
			produces = JSON
	) DetailedFriendship[] getDetailedFriendList(Authentication authentication,
												 @RequestParam("page") int page,
												 @RequestParam("size") int size) {

 		final Long id = getID(authentication.getName());
		BiFunction<Long, Long, Long> chooser = (a, b) -> a.equals(id) ? b : a;

		return friendshipService
				.getAllUserRelations(id, page, size)
				.stream().map(f -> new DetailedFriendship(f.getId(), chooser.apply(f.getUser1(), f.getUser2())))
		.toArray(DetailedFriendship[]::new);
	}



	/**
	 * <h1>Friendship response JSON:</h1>
	 *	<h2>
	 * 	[&nbsp;
	 * 		{
	 * 			"friendship": 	LONG, &nbsp;
	 * 			"timestamp": 	DATE/LONG, &nbsp;
	 * 			"user_1": 		LONG, &nbsp;
	 * 			"user_2": 		LONG
	 *		}
	 *	&nbsp;]</h2>
	 *	@see Friendship
	 *
	 */
	public @RequestMapping(
			value = "/detailed",
			method = GET,
			produces = JSON
	) Friendship getDetailedFriendship(Authentication authentication,
									   @RequestParam("id") Long relation) {

 		final Long id = getID(authentication.getName());
 		if (friendshipService.isExists(relation)) {
			Friendship friendship = friendshipService.getById(relation);
			if (friendship.getUser2().equals(id) || friendship.getUser1().equals(id))
				return friendship.clone();
		}
		throw new AccessDeniedException("Wrong friendship id");
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/add",
			method = {GET, POST}
	) void addFriend(Authentication authentication,
					 @RequestParam("user_id") Long target) {

 		final Long id = getID(authentication.getName());

 		if (friendshipService.isExistsBetweenUsers(id, target)) return;
		if (friendNotificationService.isExistsBetweenUsers(id, target)) return;

		friendNotificationService.addNotification(id, target);
		updateNotificationService.addNotification(target, FRIEND_REQUEST, id);
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = {GET, POST, DELETE}
	) void removeFriend(Authentication authentication,
						@RequestParam("user_id") Long target) {

 		final Long id = getID(authentication.getName());
		if (!friendshipService.isExistsBetweenUsers(id, target)) return;

		Friendship friendship = friendshipService.getByUsers(id, target);
		if (friendship.getUser1().equals(id) || friendship.getUser2().equals(id)) {
			friendshipService.deleteRelationBetweenUsers(id, target);
			updateNotificationService.addNotification(target, FRIEND_DELETED, id);
		}
	}



	public @RequestMapping(
			value = "/request/accept",
			method = {GET, POST}
	) Long acceptFriendRequest(Authentication authentication,
							   @RequestParam("user_id") Long target) {

 		final Long id = getID(authentication.getName());
		if (!checkNotificationRequest(id, target)) return -1L;

		Long relation = friendshipService.addFriendshipRelation(target, id);
		friendNotificationService.deleteByUsers(id, target);
		updateNotificationService.addNotification(target, FRIEND_ACCEPTED, id);
		return relation;
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/request/decline",
			method = {GET, POST}
	) void declineFriendRequest(Authentication authentication,
								@RequestParam("user_id") Long target) {

 		final Long id = getID(authentication.getName());
 		if (!checkNotificationRequest(id, target)) return;

 		friendNotificationService.deleteByUsers(id, target);
		updateNotificationService.addNotification(target, FRIEND_DECLINED, id);
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/request/direct/delete",
			method = {GET, POST, DELETE}
	) void deleteFriendRequest(Authentication authentication,
							   @RequestParam("id") Long requestId) {

		final Long id = getID(authentication.getName());
		if (!friendNotificationService.isExists(requestId)) return;
		if (!friendNotificationService.getById(requestId).getInitiatorId().equals(id)) return;

		friendNotificationService.deleteById(requestId);
	}



	/**
	 * <h1>Friendship notification response JSON:</h1>
	 *	<h2>
	 * 	[&nbsp;
	 * 		{
	 * 			"notification": 	LONG, &nbsp;
	 * 			"from": 			LONG, &nbsp;
	 * 			"to": 				LONG, &nbsp;
	 * 			"timestamp": 		DATE/LONG
	 *		}
	 *	&nbsp;]</h2>
	 *	@see FriendshipNotification
	 *
	 */
	public @RequestMapping(
			value = "/request/list/outcome",
			method = GET
	) FriendshipNotification[] getOutcomeRequestList(Authentication authentication,
													 @RequestParam("page") int page,
													 @RequestParam("size") int size) {

		return friendNotificationService.getAllNotificationByInitiator(
				getID(authentication.getName()), page, size
		).toArray(new FriendshipNotification[0]);
	}



	/**
	 * <h1>Friendship notification response JSON:</h1>
	 *	<h2>
	 * 	[&nbsp;
	 * 		{
	 * 			"notification": 	LONG, &nbsp;
	 * 			"from": 			LONG, &nbsp;
	 * 			"to": 				LONG, &nbsp;
	 * 			"timestamp": 		DATE/LONG
	 *		}
	 *	&nbsp;]</h2>
	 *	@see FriendshipNotification
	 *
	 */
	public @RequestMapping(
			value = "/request/list/income",
			method = GET
	) FriendshipNotification[] getIncomeRequestList(Authentication authentication,
												   @RequestParam("page") int page,
												   @RequestParam("size") int size) {

 		return friendNotificationService.getAllNotificationByReceiver(
				getID(authentication.getName()), page, size
		).toArray(new FriendshipNotification[0]);
	}




	private static Long getID(String name) {
		return Long.decode(name);
	}


	private boolean checkNotificationRequest(Long id, Long target) {
		boolean pre = !friendshipService.isExistsBetweenUsers(id, target)
				&& friendNotificationService.isExistsBetweenUsers(id, target);
		return pre && friendNotificationService.getWithUsers(id, target).getReceiverId().equals(id);
	}

}