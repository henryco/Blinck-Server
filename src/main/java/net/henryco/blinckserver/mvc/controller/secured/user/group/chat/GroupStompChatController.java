package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.PartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
final class GroupStompChatServicePack {

	protected final UpdateNotificationService notificationService;
	protected final PartyConversationService conversationService;
	protected final SimpMessagingTemplate messagingTemplate;
	protected final PartyService partyService;

	@Autowired
	public GroupStompChatServicePack(UpdateNotificationService notificationService,
									 PartyConversationService conversationService,
									 SimpMessagingTemplate messagingTemplate,
									 PartyService partyService) {

		this.notificationService = notificationService;
		this.conversationService = conversationService;
		this.messagingTemplate = messagingTemplate;
		this.partyService = partyService;
	}
}


/**
 * @author Henry on 18/09/17.
 */
@Controller
public class GroupStompChatController implements WebSocketConstants, BlinckNotification {


	private final GroupStompChatServicePack servicePack;

	@Autowired
	public GroupStompChatController(GroupStompChatServicePack servicePack) {
		this.servicePack = servicePack;
	}


}