package net.henryco.blinckserver.mvc.controller.secured.user.group.chat.sub;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Henry on 18/09/17.
 */
public abstract class SubGroupMessageController implements BlinckController {

	private final SubPartyService subPartyService;
	private final UpdateNotificationService notificationService;

	public SubGroupMessageController(SubPartyService subPartyService,
									 UpdateNotificationService notificationService) {
		this.subPartyService = subPartyService;
		this.notificationService = notificationService;
	}

	protected void accessCheck(Long subPartyId, Long userId)
			throws AccessDeniedException {
		if (!subPartyService.isExistsWithUser(subPartyId, userId))
			throw new AccessDeniedException("Wrong user or conversation ID");
	}

	protected final void sendMessageNotification(Long subPartyId, String notificationType) {
		final Long[] users = subPartyService.getSubPartyUsers(subPartyId);
		sendMessageNotification(users, subPartyId, notificationType);
	}

	protected final void sendMessageNotification(Long[] users, Long subPartyId, String notificationType) {
		for (Long user : users) {
			notificationService.addNotification(
					user,
					notificationType,
					subPartyId.toString()
			);
		}
	}

}