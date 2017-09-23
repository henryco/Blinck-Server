package net.henryco.blinckserver.mvc.controller.secured.user.friendship.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import static net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants.DestinationAPI.Postfix.STAT;
import static net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants.ExternalAPI.FRIENDSHIP;
import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;

/**
 * @author Henry on 10/09/17.
 */
@Controller
public class UserFriendsStompChatController
		extends FriendshipMessageController
		implements WebSocketConstants, BlinckNotification {


	private final UpdateNotificationService notificationService;
	private final FriendshipConversationService conversationService;
	private final FriendshipService friendshipService;
	private final SimpMessagingTemplate messagingTemplate;


	@Autowired
	public UserFriendsStompChatController(UpdateNotificationService notificationService,
										  FriendshipConversationService conversationService,
										  FriendshipService friendshipService,
										  SimpMessagingTemplate messagingTemplate) {
		this.notificationService = notificationService;
		this.conversationService = conversationService;
		this.friendshipService = friendshipService;
		this.messagingTemplate = messagingTemplate;
	}


	/*
	 *	WebSocket CONNECTION:
	 *
	 *		SUBSCRIBE:	/user/message/friendship/{friendship_id}	<-- GET NEW MESSAGES
	 *		SUBSCRIBE:	/user/message/friendship/stat				<-- GET MESSAGE STATUS
	 *
	 *		SEND:		/app/message/friendship						<-- SEND JSON MESSAGE
	 *
	 *
	 *	SEND message JSON:
	 *
	 *		"topic":		LONG,
	 *		"message":		CHAR[512],
	 *		"timestamp": 	DATE/LONG
	 *
	 *
	 *	GET message JSON:
	 *
	 *		"topic":	 	LONG,
	 *		"author": 		LONG,
	 *		"message": 		CHAR[512],
	 *		"timestamp": 	DATE/LONG
	 *
	 *
	 *	RESPONSE JSON:
	 *
	 * 		"destination":	CHAR[255],
	 * 		"timestamp":	DATE/LONG,
	 * 		"status":		BOOLEAN
	 *
	 */


	/**
	 * <h1>Friendship conversation INCOME JSON:</h1>
	 *	<h3>
	 * 	{&nbsp;
	 * 			"friendship":	LONG, &nbsp;
	 * 			"message": 		CHAR[512], &nbsp;
	 * 			"timestamp":	DATE/LONG
	 *	&nbsp;}</h3>
	 *	@see FriendshipConversation
	 */
	@MessageMapping({FRIENDSHIP})
	@SendToUser(FRIENDSHIP + STAT)
	public WebSocketStatusJson sendMessage(Authentication authentication,
										   MessageForm post) {

	 	final Long id = longID(authentication);

		if (!friendshipService.existsRelationWithUser(post.getTopic(), id))
			createResponse(post, false);

		processMessage(id, post);

		return createResponse(post, true);
	}



	private void processMessage(Long id, MessageForm post) {

	 	final FriendshipConversation message = conversationService.sendMessage(post, id);

		final Long friendship = post.getTopic();
		final Long secondUser = friendshipService.getSecondUser(friendship, id);
		final String destination = ExternalAPI.getFriendship(friendship);

		messagingTemplate.convertAndSendToUser(secondUser.toString(), destination, new MessageForm(message));
		notificationService.addNotification(secondUser, TYPE.FRIEND_MESSAGE_STOMP, friendship.toString());
	}



	private static
	WebSocketStatusJson createResponse(MessageForm post, boolean status) {
		return new WebSocketStatusJson(post.getTopic().toString(), post.getDate(), status);
	}


}