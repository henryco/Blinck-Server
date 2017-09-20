package net.henryco.blinckserver.mvc.controller.secured.user.group.chat.sub;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.user.group.chat.BlinckConversationController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static net.henryco.blinckserver.mvc.service.relation.conversation.SubPartyConversationService.MessageForm;

@Component
final class SubGroupConversationServicePack {

	protected final SubPartyConversationService conversationService;
	protected final UpdateNotificationService notificationService;
	protected final SubPartyService subPartyService;

	@Autowired
	public SubGroupConversationServicePack(SubPartyConversationService conversationService,
										   UpdateNotificationService notificationService,
										   SubPartyService subPartyService) {
		this.conversationService = conversationService;
		this.notificationService = notificationService;
		this.subPartyService = subPartyService;
	}
}


@RestController // TODO: 18/09/17 Tests
@RequestMapping(BlinckController.EndpointAPI.SUB_GROUP_CONVERSATION)
public class SubGroupConversationController
		extends SubGroupMessageController
		implements BlinckNotification, BlinckConversationController {

	private final SubGroupConversationServicePack servicePack;

	@Autowired
	public SubGroupConversationController(SubGroupConversationServicePack servicePack) {
		super(servicePack.subPartyService, servicePack.notificationService);
		this.servicePack = servicePack;
	}


	/*
	 *	SubGroup conversation API
	 *
	 *		ENDPOINT: 		/protected/user/subgroup/conversation
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
	 * 		SEND:
	 *
	 * 			ENDPOINT:	/messages/send
	 * 			BODY:		MessageForm
	 * 			METHOD:		POST
	 * 			RETURN:		VOID
	 *
	 */


	public @Override @RequestMapping(
			value = "/messages/count",
			method = GET
	) Long countMessages(Authentication authentication,
						 @RequestParam("id") Long subPartyId) {

		accessCheck(subPartyId, longID(authentication));
		return servicePack.conversationService.countByTopic(subPartyId);
	}


	/**
	 * <h1>SubParty Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"sub_party": 	LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 *	@see SubPartyConversationService.MessageForm
	 */
	public @Override @ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(Authentication authentication,
					   @RequestBody MessageForm messageForm) {

		final Long id = longID(authentication);
		accessCheck(messageForm.getTopic(), id);
		servicePack.conversationService.sendMessage(messageForm, id);
		sendMessageNotification(messageForm.getTopic(), TYPE.SUB_PARTY_MESSAGE_REST);
	}


	/**
	 * <h1>SubParty Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"sub_party": 	LONG, 		&nbsp;
	 *		"author": 		LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 *	@see SubPartyConversationService.MessageForm
	 */
	public @Override @RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) MessageForm[] getAllMessages(Authentication authentication,
								   @RequestParam("id") Long subPartyId,
								   @RequestParam("page") int page,
								   @RequestParam("size") int size) {

		accessCheck(subPartyId, longID(authentication));
		return servicePack.conversationService.getLastNByTopic(subPartyId, page, size);
	}

}