package net.henryco.blinckserver.mvc.controller.secured.user;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.mvc.service.relation.queue.FriendshipNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 04/09/17.
 */ @RestController
@RequestMapping("/protected/user/friends")
public class UserFriendsController implements BlinckController {


 	private final FriendshipNotificationService notificationService;
	private final FriendshipService friendshipService;


 	@Autowired
	public UserFriendsController(FriendshipService friendshipService,
								 FriendshipNotificationService notificationService) {
		this.notificationService = notificationService;
		this.friendshipService = friendshipService;
	}



	public @RequestMapping(
			value = "/list/{page}/{size}",
			method = GET,
			produces = JSON
	) Long[] getFriendList(Authentication authentication,
						   @PathVariable("page") int page,
						   @PathVariable("size") int size) {

		final Long id = getID(authentication.getName());
		BiFunction<Long, Long, Long> chooser = (a, b) -> a.equals(id) ? b : a;

		return friendshipService
				.getAllUserRelations(id, page, size)
				.stream().map(f -> chooser.apply(f.getUser1(), f.getUser2()))
		.toArray(Long[]::new);
	}




	public @ResponseStatus(OK) @RequestMapping(
			value = "/add/{user_id}",
			method = {GET, POST}
	) void addFriend(Authentication authentication,
					 @PathVariable("user_id") Long target) {

 		final Long id = getID(authentication.getName());

 		if (friendshipService.isExistsBetweenUsers(id, target)) return;
		if (notificationService.isExistsBetweenUsers(id, target)) return;

		notificationService.addNotification(id, target);
	}




	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove/{user_id}",
			method = {GET, POST}
	) void removeFriend(Authentication authentication,
						@PathVariable("user_id") Long target) {

 		final Long id = getID(authentication.getName());
		if (!friendshipService.isExistsBetweenUsers(id, target)) return;

		Friendship friendship = friendshipService.getByUsers(id, target);
		if (friendship.getUser1().equals(id) || friendship.getUser2().equals(id))
			friendshipService.deleteRelationBetweenUsers(id, target);
	}




	public @ResponseStatus(OK) @RequestMapping(
			value = "/request/{user_id}/accept",
			method = {GET, POST}
	) void acceptFriendRequest(Authentication authentication,
							   @PathVariable("user_id") Long target) {

 		final Long id = getID(authentication.getName());
		if (!checkNotificationRequest(id, target)) return;

		friendshipService.addFriendshipRelation(target, id);
		notificationService.deleteByUsers(id, target);
	}




	public @ResponseStatus(OK) @RequestMapping(
			value = "/request/{user_id}/decline",
			method = {GET, POST}
	) void declineFriendRequest(Authentication authentication,
								@PathVariable("user_id") Long target) {

 		final Long id = getID(authentication.getName());
 		if (!checkNotificationRequest(id, target)) return;

 		notificationService.deleteByUsers(id, target);
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/request/delete/{request_id}",
			method = {GET, POST}
	) void deleteFriendRequest(Authentication authentication,
							   @PathVariable("request_id") Long reqId) {

		final Long id = getID(authentication.getName());
		if (!notificationService.isExists(reqId)) return;
		if (!notificationService.getById(reqId).getInitiatorId().equals(id)) return;

		notificationService.deleteById(reqId);
	}





	private static Long getID(String name) {
		return Long.decode(name);
	}


	private boolean checkNotificationRequest(Long id, Long target) {
		boolean pre = !friendshipService.isExistsBetweenUsers(id, target)
				&& notificationService.isExistsBetweenUsers(id, target);
		return pre && notificationService.getWithUsers(id, target).getReceiverId().equals(id);
	}

}