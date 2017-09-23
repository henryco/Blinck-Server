package net.henryco.blinckserver.mvc.controller.secured.user.group.chat.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.secured.user.group.chat.BlinckStompChatController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.PartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;
import static net.henryco.blinckserver.mvc.service.relation.core.PartyService.PartyInfo;


@Component
final class GroupStompChatServicePack {

	protected final UpdateNotificationService notification;
	protected final PartyConversationService conversation;
	protected final SimpMessagingTemplate messaging;
	protected final PartyService party;

	@Autowired
	public GroupStompChatServicePack(UpdateNotificationService notificationService,
									 PartyConversationService conversationService,
									 SimpMessagingTemplate messagingTemplate,
									 PartyService partyService) {

		this.notification = notificationService;
		this.conversation = conversationService;
		this.messaging = messagingTemplate;
		this.party = partyService;
	}

}


/**
 * @author Henry on 18/09/17.
 */
@Controller // TODO: 20/09/17 TESTS
public class GroupStompChatController
		implements BlinckStompChatController, BlinckNotification {


	private final GroupStompChatServicePack services;

	@Autowired
	public GroupStompChatController(GroupStompChatServicePack servicePack) {
		this.services = servicePack;
	}


	/*
	 *	WebSocket CONNECTION:
	 *
	 *		SUBSCRIBE:	/user/message/group/{group_id}			<-- GET NEW MESSAGES
	 *		SUBSCRIBE:	/user/message/group/stat				<-- GET MESSAGE STATUS
	 *
	 *		SEND:		/app/message/group						<-- SEND JSON MESSAGE
	 *
	 *
	 *	SEND message JSON:
	 *
	 *		"topic":		LONG,
	 *		"message":		CHAR[512],
	 *		"timestamp": 	DATE/LONG
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
	 * <h1>Party Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"topic": 		LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 *	@see MessageForm
	 */
	@Override @MessageMapping({ExternalAPI.GROUP})
	@SendToUser(ExternalAPI.GROUP + DestinationAPI.Postfix.STAT)
	public WebSocketStatusJson sendMessage(Authentication authentication,
										   MessageForm messageForm) {

		final Long id = longID(authentication);
		final PartyInfo partyInfo = services.party.getPartyInfo(messageForm.getTopic(), id);

		if (!partyInfo.isActiveAndContainsUser(id))
			return createResponse(messageForm, messageForm.getDate(), false);

		MessageForm processed = processMessage(id, messageForm, partyInfo);
		return createResponse(processed, messageForm.getDate(), true);
	}


	private MessageForm processMessage(Long userId, MessageForm message, PartyInfo info) {

		MessageForm form = saveMessage(services.conversation, message, userId);

		final Long party = form.getTopic();
		final Long[] users = info.getUsers().toArray(new Long[0]);
		final String destination = ExternalAPI.getGroup(party);

		stompSend(services.messaging, users, destination, form);
		sendMessageNotification(users, party, TYPE.PARTY_MESSAGE_STOMP);

		return form;
	}


	private void sendMessageNotification(Long[] users, Long partyId, String type) {
		for (Long user : users)
			services.notification.addNotification(user, type, partyId.toString());
	}

}