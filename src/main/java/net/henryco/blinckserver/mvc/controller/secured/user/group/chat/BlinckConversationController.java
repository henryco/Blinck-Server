package net.henryco.blinckserver.mvc.controller.secured.user.group.chat;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 20/09/17.
 */
public interface BlinckConversationController extends BlinckController {


	/*
	 *	Conversation API
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


	@RequestMapping(
			value = "/messages/count",
			method = GET
	) Long countMessages(
			Authentication authentication,
			@RequestParam("id") Long topic
	);


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
	@ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(
			Authentication authentication,
			@RequestBody MessageForm messageForm
	);


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
	@RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) MessageForm[] getAllMessages(
			Authentication authentication,
			@RequestParam("id") Long topic,
			@RequestParam("page") int page,
			@RequestParam("size") int size
	);

}