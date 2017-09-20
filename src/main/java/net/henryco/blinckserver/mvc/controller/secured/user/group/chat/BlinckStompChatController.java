package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;

import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;

import java.util.Date;

/**
 * @author Henry on 20/09/17.
 */
public interface BlinckStompChatController
		extends WebSocketConstants, BlinckController {


	WebSocketStatusJson sendMessage(Authentication authentication, MessageForm messageForm);


	default WebSocketStatusJson
	createResponse(MessageForm post, Date date, boolean status) {
		return new WebSocketStatusJson(post.getTopic().toString(), date, status);
	}


	default void
	stompSend(SimpMessagingTemplate template, Long[] users, String destination, Object payload) {
		for (Long user: users)
			template.convertAndSendToUser(user.toString(), destination, payload);
	}

	default MessageForm
	saveMessage(ConversationService service, MessageForm messageForm, Long userId) {
		return new MessageForm(service.sendMessage(messageForm, userId));
	}
}
