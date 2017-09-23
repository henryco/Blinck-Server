package net.henryco.blinckserver.mvc.controller.secured.user.group.chat.sub;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.secured.user.group.chat.BlinckStompChatController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

import static net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService.MessageForm;


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
@Controller // TODO: 18/09/17 Tests 
public class SubGroupStompChatController
		extends SubGroupMessageController
		implements BlinckStompChatController, BlinckNotification {

	private final SubGroupStompChatServicePack servicePack;

	@Autowired
	public SubGroupStompChatController(SubGroupStompChatServicePack servicePack) {
		super(servicePack.subPartyService, servicePack.notificationService);
		this.servicePack = servicePack;
	}


	/*
	 *	WebSocket CONNECTION:
	 *
	 *		SUBSCRIBE:	/user/message/subgroup/{subgroup_id}	<-- GET NEW MESSAGES
	 *		SUBSCRIBE:	/user/message/subgroup/stat				<-- GET MESSAGE STATUS
	 *
	 *		SEND:		/app/message/subgroup					<-- SEND JSON MESSAGE
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
	 * <h1>Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"topic": 	LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 *	@see MessageForm
	 */
	@Override @MessageMapping({ExternalAPI.SUBGROUP})
	@SendToUser(ExternalAPI.SUBGROUP + DestinationAPI.Postfix.STAT)
	public WebSocketStatusJson sendMessage(Authentication authentication,
										   MessageForm messageForm) {

		final Long id = longID(authentication);

		if (!servicePack.subPartyService.isExistsWithUser(messageForm.getTopic(), id))
			return createResponse(messageForm, messageForm.getDate(), false);

		MessageForm processed = processMessage(id, messageForm);
		return createResponse(processed, messageForm.getDate(), true);
	}




	private MessageForm processMessage(Long userId, MessageForm messageForm) {

		MessageForm form = saveMessage(servicePack.conversationService, messageForm, userId);

		final Long subParty = form.getTopic();
		final Long[] users = servicePack.subPartyService.getSubPartyUsers(subParty);
		final String destination = ExternalAPI.getSubgroup(subParty);

		stompSend(servicePack.messagingTemplate, users, destination, form);
		sendMessageNotification(users, subParty, TYPE.SUB_PARTY_MESSAGE_STOMP);

		return form;
	}

}