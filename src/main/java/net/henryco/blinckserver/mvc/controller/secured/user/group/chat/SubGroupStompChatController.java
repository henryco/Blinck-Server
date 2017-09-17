package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
final class SubGroupStompChatServicePack {

	protected final SubPartyConversationService conversationService;
	protected final UpdateNotificationService notificationService;
	protected final SimpMessagingTemplate messagingTemplate;
	protected final SubPartyService subPartyService;

	@Autowired
	public SubGroupStompChatServicePack(SubPartyConversationService conversationService,
										UpdateNotificationService notificationService,
										SimpMessagingTemplate messagingTemplate,
										SubPartyService subPartyService) {
		this.conversationService = conversationService;
		this.notificationService = notificationService;
		this.messagingTemplate = messagingTemplate;
		this.subPartyService = subPartyService;
	}
}


/**
 * @author Henry on 18/09/17.
 */
@Controller
public class SubGroupStompChatController implements WebSocketConstants, BlinckNotification {

	private final SubGroupStompChatServicePack servicePack;

	@Autowired
	public SubGroupStompChatController(SubGroupStompChatServicePack servicePack) {
		this.servicePack = servicePack;
	}


}