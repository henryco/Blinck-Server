package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.PartyConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
final class GroupConversationServicePack {

	protected final UpdateNotificationService notificationService;
	protected final PartyConversationService conversationService;

	@Autowired
	GroupConversationServicePack(UpdateNotificationService notificationService,
								 PartyConversationService conversationService) {
		this.notificationService = notificationService;
		this.conversationService = conversationService;
	}
}

/**
 * @author Henry on 18/09/17.
 */
@RestController
@RequestMapping(BlinckController.EndpointAPI.GROUP_CONVERSATION)
public class GroupConversationController implements BlinckController {

	private final GroupConversationServicePack servicePack;

	@Autowired
	public GroupConversationController(GroupConversationServicePack servicePack) {
		this.servicePack = servicePack;
	}

}

