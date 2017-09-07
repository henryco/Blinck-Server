package net.henryco.blinckserver.mvc.controller.secured.user.notifications;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *  @author Henry on 06/09/17.
 */ @RestController
@RequestMapping("/protected/user/notifications")
public class UserNotificationsController implements BlinckController {

 	private final UpdateNotificationService service;

 	@Autowired
	public UserNotificationsController(
			UpdateNotificationService service) {
		this.service = service;
	}


	/**
	 * <h1>NotificationForm JSON</h1>
	 * <h2>
	 *     {&nbsp;
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     &nbsp;}
	 * </h2>
	 */
	@Data @NoArgsConstructor
	private static final class NotificationForm
			implements Serializable{

 		private Long id;
 		private String type;
 		private String info;
 		private Date timestamp;

		private NotificationForm(
				UpdateNotification notification) {
			this.id = notification.getId();
			this.type = notification.getDetails().getType();
			this.info = notification.getDetails().getNotification();
			this.timestamp = notification.getDate();
		}

	}


	private static NotificationForm[]
	streamToArray(Stream<UpdateNotification> stream) {
		return stream.map(NotificationForm::new).toArray(NotificationForm[]::new);
	}




	public @RequestMapping(
			value = "/count",
			method = GET
	) Long countAllNotifications(Authentication authentication) {
 		return service.countAllUserNotifications(longID(authentication));
	} // Tested



	/**
	 * <h1>NotificationForm ARRAY JSON</h1>
	 * <h2>
	 * 	[&nbsp;
	 * 		{
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     	}
	 * 	&nbsp;]
	 * </h2>
	 * @see NotificationForm
	 */
	public @RequestMapping(
			value = "/list",
			method = GET,
			produces = JSON
	) NotificationForm[] getAllNotifications(Authentication authentication,
											 @RequestParam("page") int page,
											 @RequestParam("size") int size) {

		final Long id = longID(authentication);
		return streamToArray(service.getAllUserNotifications(id, page, size).stream());
	}



	/**
	 * <h1>NotificationForm ARRAY JSON</h1>
	 * <h2>
	 * 	[&nbsp;
	 * 		{
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     	}
	 * 	&nbsp;]
	 * </h2>
	 * @see NotificationForm
	 */
	public @RequestMapping(
			value = "/list/all",
			method = GET,
			produces = JSON
	) NotificationForm[] getAllNotifications(Authentication authentication) {

		final Long id = longID(authentication);
		return streamToArray(service.getAllUserNotifications(id).stream());
	}



	/**
	 * <h1>NotificationForm ARRAY JSON</h1>
	 * <h2>
	 * 	[&nbsp;
	 * 		{
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     	}
	 * 	&nbsp;]
	 * </h2>
	 * @see NotificationForm
	 */
	public @RequestMapping(
			value = "/list/all/pop",
			method = GET,
			produces = JSON
	) NotificationForm[] popAllNotifications(Authentication authentication) {

		final Long id = longID(authentication);
		return streamToArray(service.popAllNotifications(id).stream());
	}



	/**
	 * <h1>NotificationForm JSON</h1>
	 * <h2>
	 *     {&nbsp;
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     &nbsp;}
	 * </h2>
	 * @see NotificationForm
	 */
	public @RequestMapping(
			value = "/last",
			method = GET,
			produces = JSON
	) NotificationForm getLastNotification(Authentication authentication) {
 		NotificationForm[] last = getAllNotifications(authentication, 0, 1);
		return last.length == 0 ? null : last[0];
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = {DELETE, POST, GET}
	) void removeNotification(Authentication authentication,
							  @RequestParam("id") Long notificationId) {

		if (!service.isExists(notificationId))
			return;

		UpdateNotification byId = service.getById(notificationId);
		if (!byId.getTargetUserId().equals(longID(authentication)))
			return;

		service.deleteById(notificationId);
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove/all",
			method = {DELETE, POST, GET}
	) void removeAllNotifications(Authentication authentication) {
 		service.removeAllUserNotifications(longID(authentication));
	}


}