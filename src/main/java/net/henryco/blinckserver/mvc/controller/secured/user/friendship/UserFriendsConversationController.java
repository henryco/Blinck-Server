package net.henryco.blinckserver.mvc.controller.secured.user.friendship;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 05/09/17.
 */ @RestController
@RequestMapping("/protected/user/friends/conversation")
public class UserFriendsConversationController implements BlinckController {


 	private final FriendshipConversationService conversationService;
	private final FriendshipService friendshipService;


	@Autowired
	public UserFriendsConversationController(FriendshipConversationService conversationService,
											 FriendshipService friendshipService) {
		this.conversationService = conversationService;
		this.friendshipService = friendshipService;
	}



	// TESTED
	public @RequestMapping(
			value = "/messages/count",
			method = GET
	) Long getAllMessagesCount(Authentication authentication,
							   @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, getID(authentication.getName()));
		return conversationService.countByFriendshipId(friendshipId);
	}




	/**
	 * <h1>Friendship conversation response JSON:</h1>
	 *	<h3>
	 * 	[&nbsp;
	 * 		{
	 * 			"id": 			LONG, &nbsp;
	 * 			"message": 		CHAR[512], &nbsp;
	 * 			"timestamp": 	DATE/LONG, &nbsp;
	 * 			"author": 		LONG, &nbsp;
	 * 			"friendship":	LONG
	 *		}
	 *	&nbsp;]</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @RequestMapping(
			value = "/messages/list",
			method = GET,
			produces = JSON
	) FriendshipConversation[] getAllMessages(Authentication authentication,
											  @RequestParam("page") int page,
											  @RequestParam("size") int size,
											  @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, getID(authentication.getName()));
 		return conversationService.getByFriendshipId(friendshipId, page, size)
				.toArray(new FriendshipConversation[0]);
	}




	/**
	 * <h1>Friendship conversation POST JSON:</h1>
	 *	<h3>
	 * 	{&nbsp;
	 * 			"friendship":	LONG, &nbsp;
	 * 			"message": 		CHAR[512]
	 *	&nbsp;}</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @ResponseStatus(OK) @RequestMapping(
			value = "/messages/send",
			method = POST,
			consumes = JSON
	) void sendMessage(Authentication authentication,
					   @RequestBody FriendshipConversation post) {

		final Long id = getID(authentication.getName());
		accessCheck(friendshipService, post.getFriendship(), id);

		FriendshipConversation message = new FriendshipConversation();
		message.setDate(new Date(System.currentTimeMillis()));
		message.setFriendship(post.getFriendship());
		message.setMessage(post.getMessage());
		message.setAuthor(id);

		conversationService.save(message);
	}




	/**
	 * <h1>Friendship conversation response JSON:</h1>
	 *	<h3>
	 * 	&nbsp;{
	 * 			"id": 			LONG, &nbsp;
	 * 			"message": 		CHAR[512], &nbsp;
	 * 			"timestamp": 	DATE/LONG, &nbsp;
	 * 			"author": 		LONG, &nbsp;
	 * 			"friendship":	LONG
	 *	&nbsp;}
	 *	</h3>
	 *	@see FriendshipConversation
	 */ // TESTED
	public @RequestMapping(
			value = "/messages/last",
			method = GET,
			produces = JSON
	) FriendshipConversation getLastMessage(Authentication authentication,
											@RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, getID(authentication.getName()));

		List<FriendshipConversation> list =
				conversationService.getByFriendshipId(friendshipId, 0, 1);
		return list.isEmpty() ? null : list.get(0);
	}



	// TESTED
	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = DELETE
	) void removeAllMessages(Authentication authentication,
							 @RequestParam("id") Long friendshipId) {

		accessCheck(friendshipService, friendshipId, getID(authentication.getName()));
		conversationService.deleteAllByFriendshipId(friendshipId);
	}





	private static
	Long getID(String name) {
		return Long.decode(name);
	}

	private static
	void accessCheck(FriendshipService friendshipService, Long friendshipId, Long userId) {
		if (!friendshipService.existsRelationWithUser(friendshipId, userId))
			throw new AccessDeniedException("Wrong user or conversation ID");
	}

}