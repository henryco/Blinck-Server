package net.henryco.blinckserver.mvc.controller.secured.user.friendship.chat;


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

import static net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants.Destination.Postfix.STAT;
import static net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants.Service.FRIENDSHIP;


/**
 * @author Henry on 10/09/17.
 */
@Controller
public class FriendshipChatController
		extends FriendshipMessageController
		implements WebSocketConstants {


	private final UpdateNotificationService notificationService;
	private final FriendshipConversationService conversationService;
	private final FriendshipService friendshipService;
	private final SimpMessagingTemplate messagingTemplate;


	@Autowired
	public FriendshipChatController(UpdateNotificationService notificationService,
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
							  FriendshipConversation post) {

	 	final Long id = longID(authentication);

		if (!friendshipService.existsRelationWithUser(post.getFriendship(), id))
			createResponse(post, false);

		processMessage(id, post);

		return createResponse(post, true);
	}



	private void processMessage(Long id, FriendshipConversation post) {

	 	final FriendshipConversation message = conversationService.save(createMessage(id, post));

		final Long friendship = post.getFriendship();
		final Long secondUser = friendshipService.getSecondUser(friendship, id);
		final String destination = Service.getFriendship(friendship);

		messagingTemplate.convertAndSendToUser(secondUser.toString(), destination, message.clone());
		notificationService.addNotification(secondUser, TYPE.FRIEND_MESSAGE, friendship.toString());
	}



	private static
	WebSocketStatusJson createResponse(FriendshipConversation post, boolean status) {
		return new WebSocketStatusJson(post.getFriendship().toString(), post.getDate(), status);
	}


}