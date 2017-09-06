package net.henryco.blinckserver.mvc.controller.secured.user.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;

/**
 *  @author Henry on 06/09/17.
 */ @RestController
@RequestMapping("/protected/user/notifications")
public class UserNotificationsController implements BlinckController {

 	private final UpdateNotificationService updateNotificationService;

 	@Autowired
	public UserNotificationsController(
			UpdateNotificationService updateNotificationService) {
		this.updateNotificationService = updateNotificationService;
	}


	@Data @NoArgsConstructor
	private static final class NotificationForm
			implements Serializable{

 		private Long id;
 		private Long user;
 		private String type;
 		private String info;
		private Date timestamp;

		private NotificationForm(
				UpdateNotification notification) {
			this.id = notification.getId();
			this.user = notification.getTargetUserId();
			this.type = notification.getDetails().getType();
			this.info = notification.getDetails().getNotification();
			this.timestamp = notification.getDate();
		}

	}






}