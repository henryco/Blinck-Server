package net.henryco.blinckserver.mvc.controller.secured.user.infrastructure;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 *  @author Henry on 06/09/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.USER_NOTIFICATIONS)
public class UserNotificationsController
		implements BlinckController, BlinckNotification {

 	private final UpdateNotificationService service;

 	@Autowired
	public UserNotificationsController(
			UpdateNotificationService service) {
		this.service = service;
	}


	private static JsonNotificationForm[]
	streamToArray(Stream<UpdateNotification> stream) {
		return stream.map(JsonNotificationForm::new).toArray(JsonNotificationForm[]::new);
	}


	/*
	 *	Update notification API
	 *
	 *		ENDPOINT: 		/protected/user/notifications
	 *
	 *
	 *	JsonNotificationForm:
	 *
	 *		"id": 			LONG,
	 *		"type": 		CHAR[255],
	 *		"info": 		CHAR[255],
	 *		"timestamp": 	DATE/LONG
	 *
	 *
	 *		COUNT:
	 *
	 *			ENDPOINT:	/count
	 *			METHOD:		GET
	 *			RETURN:		Long
	 *
	 *
	 *		LIST:
	 *
	 *			ENDPOINT:	/list
	 *			ARGS:		Int: page, Int: size
	 *			METHOD:		GET
	 *			RETURN:		JsonNotificationForm[]
	 *
	 *
	 * 		ALL:
	 *
	 * 			ENDPOINT:	/list/all
	 * 			METHOD:		GET
	 * 			RETURN:		JsonNotificationForm[]
	 *
	 *
	 *		POP_ALL:
	 *
	 *			ENDPOINT:	/list/all/pop
	 *			METHOD:		GET
	 *			RETURN:		JsonNotificationForm[]
	 *
	 *
	 *		LAST:
	 *
	 *			ENDPOINT:	/last
	 *			METHOD:		GET
	 *			RETURN:		JsonNotificationForm
	 *
	 *
	 *		REMOVE:
	 *
	 *			ENDPOINT:	/remove
	 *			ARGS:		Long: id
	 *			METHOD:		DELETE, POST, GET
	 *			RETURN:		VOID
	 *
	 *
	 *		REMOVE_ALL:
	 *
	 *			ENDPOINT:	/remove/all
	 *			METHOD:		DELETE, POST, GET
	 *			RETURN:		VOID
	 *
	 */


	public @RequestMapping(
			value = "/count",
			method = GET //		Tested
	) Long countAllNotifications(Authentication authentication) {
 		return service.countAllUserNotifications(longID(authentication));
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
	 * @see JsonNotificationForm
	 */
	public @RequestMapping(
			value = "/list",
			method = GET,
			produces = JSON //	Tested
	) JsonNotificationForm[] getAllNotifications(Authentication authentication,
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
	 * @see JsonNotificationForm
	 */
	public @RequestMapping(
			value = "/list/all",
			method = GET,
			produces = JSON //	Tested
	) JsonNotificationForm[] getAllNotifications(Authentication authentication) {

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
	 * @see JsonNotificationForm
	 */
	public @RequestMapping(
			value = "/list/all/pop",
			method = GET,
			produces = JSON //	Tested
	) JsonNotificationForm[] popAllNotifications(Authentication authentication) {

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
	 * @see JsonNotificationForm
	 */
	public @RequestMapping(
			value = "/last",
			method = GET,
			produces = JSON //	Tested
	)
	JsonNotificationForm getLastNotification(Authentication authentication) {

		JsonNotificationForm[] last = getAllNotifications(authentication, 0, 1);
		return last.length == 0 ? null : last[0];
	}



	public @ResponseStatus(OK) @RequestMapping(
			value = "/remove",
			method = {DELETE, POST, GET} //	Tested
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
			method = {DELETE, POST, GET} //	Tested
	) void removeAllNotifications(Authentication authentication) {
 		service.removeAllUserNotifications(longID(authentication));
	}


}