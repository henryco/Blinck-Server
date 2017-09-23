package net.henryco.blinckserver.mvc.controller.secured.user.friendship.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.user.group.chat.BlinckConversationController;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;

/**
 * @author Henry on 05/09/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.FRIENDSHIP_CONVERSATION)
public class UserFriendsConversationController
		extends FriendshipMessageController
		implements BlinckNotification, BlinckConversationController {


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
	 *	MessageForm:
	 *
	 * 		"topic": 		LONG,
	 *		"author": 		LONG,			(not required for POST)
	 * 		"message": 		CHAR[512],
	 * 		"timestamp": 	DATE/LONG		(not required for POST)
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
	 *			RETURN:		MessageForm[]
	 *
	 *
	 * 		LAST:
	 *
	 * 			ENDPOINT:	/messages/last
	 * 			ARGS:		Long: id
	 * 			METHOD:		POST
	 * 			RETURN:		MessageForm
	 *
	 *
	 * 		SEND:
	 *
	 * 			ENDPOINT:	/messages/send
	 * 			BODY:		MessageForm
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



	public @Override @RequestMapping(
			value = "/messages/count",
			method = GET
	) Long countMessages(Authentication authentication,
						 @RequestParam("id") Long topic) {
		accessCheck(friendshipService, topic, longID(authentication));
		return conversationService.countByTopic(topic);
	}



	public @Override @ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(Authentication authentication,
					   @RequestBody MessageForm messageForm) {

		final Long id = longID(authentication);
		accessCheck(friendshipService, messageForm.getTopic(), id);
		conversationService.sendMessage(messageForm, id);
		sendMessageNotification(messageForm.getTopic(), id);
	}



	public @Override @RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) MessageForm[] getAllMessages(Authentication authentication,
								   @RequestParam("id") Long topic,
								   @RequestParam("page") int page,
								   @RequestParam("size") int size) {

		accessCheck(friendshipService, topic, longID(authentication));
		return conversationService.getLastNByTopic(topic, page, size);
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
	) MessageForm getLastMessage(Authentication authentication,
											@RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));

		MessageForm[] messages = getAllMessages(authentication, friendshipId, 0, 1);
		return messages.length == 0 ? null : messages[0];
	}


	// TESTED
	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = DELETE
	) void removeAllMessages(Authentication authentication,
							 @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, longID(authentication));
		conversationService.deleteAllInTopic(friendshipId);
	}




	private void sendMessageNotification(Long friendship, Long id) {

		notificationService.addNotification(
				friendshipService.getSecondUser(friendship, id),
				TYPE.FRIEND_MESSAGE_REST,
				friendship.toString()
		);
	}

}