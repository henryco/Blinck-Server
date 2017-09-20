package net.henryco.blinckserver.mvc.controller.secured.user.group.chat.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.controller.secured.user.group.chat.BlinckConversationController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.conversation.PartyConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
final class GroupConversationServicePack {

	protected final UpdateNotificationService notification;
	protected final PartyConversationService conversation;
	protected final PartyService party;

	@Autowired
	GroupConversationServicePack(UpdateNotificationService notificationService,
								 PartyConversationService conversationService,
								 PartyService partyService) {
		this.notification = notificationService;
		this.conversation = conversationService;
		this.party = partyService;
	}
}

/**
 * @author Henry on 18/09/17.
 */
@RestController
@RequestMapping(BlinckController.EndpointAPI.GROUP_CONVERSATION)
public class GroupConversationController
		implements BlinckController, BlinckConversationController {

	private final GroupConversationServicePack services;

	@Autowired
	public GroupConversationController(GroupConversationServicePack servicePack) {
		this.services = servicePack;
	}


	/*
	 *	Group conversation API
	 *
	 *		ENDPOINT: 		/protected/user/group/conversation
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
		// TODO: 20/09/17
		return null;
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
	 *	@see MessageForm
	 */
	public @Override @ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(Authentication authentication,
					   @RequestBody MessageForm messageForm) {
		// TODO: 20/09/17
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
	 *	@see MessageForm
	 */
	public @Override @RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) MessageForm[] getAllMessages(Authentication authentication,
								   @RequestParam("id") Long subPartyId,
								   @RequestParam("page") int page,
								   @RequestParam("size") int size) {
		// TODO: 20/09/17
		return null;
	}


}