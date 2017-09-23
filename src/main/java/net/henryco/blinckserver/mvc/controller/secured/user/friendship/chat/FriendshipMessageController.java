package net.henryco.blinckserver.mvc.controller.secured.user.friendship.chat;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.springframework.security.access.AccessDeniedException;

import java.util.Date;

/**
 * @author Henry on 11/09/17.
 */
public abstract class FriendshipMessageController implements BlinckController, BlinckNotification {


	protected static void accessCheck(FriendshipService friendshipService, Long friendshipId, Long userId)
			throws AccessDeniedException {
		if (!friendshipService.existsRelationWithUser(friendshipId, userId))
			throw new AccessDeniedException("Wrong user or conversation ID");
	}


}