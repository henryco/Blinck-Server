package net.henryco.blinckserver.mvc.controller.secured.user.friendship.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Henry on 05/09/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.FRIENDSHIP_CONVERSATION)
public class UserFriendsConversationController
		extends FriendshipMessageController
		implements BlinckNotification {


 	private final UpdateNotificationService notificationService;
 	private final FriendshipConversationService conversationService;
	private final FriendshipService friendshipService;


	@Autowired
	public UserFriendsConversationController(UpdateNotificationService notificationService,
											 FriendshipConversationService conversationService,
											 FriendshipService friendshipService) {
		this.notificationService = notificationService;
		this.conversationService = conversationService;
		this.friendshipService = friendshipService;
	}



	/*
	 *	Friendship conversation API
	 *
	 *		ENDPOINT: 		/protected/user/friends/conversation
	 *
	 *
	 *	FriendshipConversation:
	 *
	 *		"id":			LONG,			(not required for POST)
	 *		"message":		CHAR[512],
	 *		"timestamp":	DATE/LONG,		(not required for POST)
	 *		"author":		LONG,			(not required for POST)
	 *		"friendship":	LONG
	 *
	 *
	 *		COUNT:
	 *
	 *			ENDPOINT:	/messages/count
	 *			ARGS:		Long: id
	 *			METHOD:		GET
	 *			RETURN:		Long
	 *
	 *
	 *		LIST:
	 *
	 *			ENDPOINT:	/messages/list
	 *			ARGS:		Int: page, Int: size, Long: id
	 *			METHOD:		GET
	 *			RETURN:		FriendshipConversation[]
	 *
	 *
	 * 		LAST:
	 *
	 * 			ENDPOINT:	/messages/last
	 * 			ARGS:		Long: id
	 * 			METHOD:		POST
	 * 			RETURN:		FriendshipConversation
	 *
	 *
	 * 		SEND:
	 *
	 * 			ENDPOINT:	/messages/send
	 * 			BODY:		FriendshipConversation
	 * 			METHOD:		POST
	 * 			RETURN:		VOID
	 *
	 *
	 * 		REMOVE:
	 *
	 * 			ENDPOINT:	/remove
	 * 			ARGS:		Long: id
	 * 			METHOD:		DELETE
	 * 			RETURN:		VOID
	 *
	 */



	// TESTED
	public @RequestMapping(
			value = "/messages/count",
			method = GET
	) Long getAllMessagesCount(Authentication authentication,
							   @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));
		return conversationService.countByFriendshipId(friendshipId);
	}



	/**
	 * <h1>Friendship conversation response ARRAY JSON:</h1>
	 *	<h3>
	 * 	[&nbsp;
	 * 		{
	 * 			"id": 			LONG, &nbsp;
	 * 			"message": 		CHAR[512], &nbsp;
	 * 			"timestamp": 	DATE/LONG, &nbsp;
	 * 			"author": 		LONG, &nbsp;
	 * 			"friendship":	LONG
	 *		}
	 *	&nbsp;]</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) FriendshipConversation[] getAllMessages(Authentication authentication,
											  @RequestParam("page") int page,
											  @RequestParam("size") int size,
											  @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));
 		return conversationService.getByFriendshipId(friendshipId, page, size)
				.toArray(new FriendshipConversation[0]);
	}



	/**
	 * <h1>Friendship conversation POST JSON:</h1>
	 *	<h3>
	 * 	{&nbsp;
	 * 			"friendship":	LONG, &nbsp;
	 * 			"message": 		CHAR[512]
	 *	&nbsp;}</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(Authentication authentication,
					   @RequestBody FriendshipConversation post) {

		final Long id = longID(authentication);
		accessCheck(friendshipService, post.getFriendship(), id);

		conversationService.save(createMessage(id, post));
		sendMessageNotification(post.getFriendship(), id);
	}



	/**
	 * <h1>Friendship conversation response JSON:</h1>
	 *	<h3>
	 * 	&nbsp;{
	 * 			"id": 			LONG, &nbsp;
	 * 			"message": 		CHAR[512], &nbsp;
	 * 			"timestamp": 	DATE/LONG, &nbsp;
	 * 			"author": 		LONG, &nbsp;
	 * 			"friendship":	LONG
	 *	&nbsp;}
	 *	</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @RequestMapping(
			value = "/messages/last",
			method = GET,
			produces = JSON
	) FriendshipConversation getLastMessage(Authentication authentication,
											@RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));

		List<FriendshipConversation> list =
				conversationService.getByFriendshipId(friendshipId, 0, 1);
		return list.isEmpty() ? null : list.get(0).clone();
	}



	// TESTED
	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = DELETE
	) void removeAllMessages(Authentication authentication,
							 @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));
		conversationService.deleteAllByFriendshipId(friendshipId);
	}




	private void sendMessageNotification(Long friendship, Long id) {

		notificationService.addNotification(
				friendshipService.getSecondUser(friendship, id),
				TYPE.FRIEND_MESSAGE_REST,
				friendship.toString()
		);
	}

}