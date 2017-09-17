package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
final class SubGroupConversationServicePack {

	protected final SubPartyConversationService conversationService;
	protected final UpdateNotificationService notificationService;

	@Autowired
	public SubGroupConversationServicePack(SubPartyConversationService conversationService,
							   UpdateNotificationService notificationService) {
		this.conversationService = conversationService;
		this.notificationService = notificationService;
	}
}

/**
 * @author Henry on 18/09/17.
 */
@RestController
@RequestMapping(BlinckController.EndpointAPI.SUB_GROUP_CONVERSATION)
public class SubGroupConversationController implements BlinckController {

	private final SubGroupConversationServicePack servicePack;

	@Autowired
	public SubGroupConversationController(SubGroupConversationServicePack servicePack) {
		this.servicePack = servicePack;
	}



}