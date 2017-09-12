package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Henry on 06/09/17.
 */
@Service
public class UpdateNotificationService
		extends BlinckDaoProvider<UpdateNotification, Long>
		implements BlinckNotification {


	private final SimpMessagingTemplate webSocketMessageTemplate;


	@Autowired
	public UpdateNotificationService(UpdateNotificationDao notificationDao,
									 SimpMessagingTemplate webSocketMessageTemplate) {
		super(notificationDao);
		this.webSocketMessageTemplate = webSocketMessageTemplate;
	}

	private UpdateNotificationDao getDao() {
		return provideDao();
	}



	/**
	 * @return <b>notification ID</b>
	 */
	@Transactional // Tested
	public Long addNotification(SimpleNotification simpleNotification) {

		UpdateNotification saved = getDao().save(createNotification(simpleNotification));
		sendNotification(webSocketMessageTemplate, saved);
		return saved.getId();
	}


	/**
	 * @return <b>notification ID</b>
	 */
	@Transactional // Tested
	public Long addNotification(Long targetUserId, String type, Serializable notification) {
		return addNotification(new SimpleNotification(targetUserId, type, notification.toString()));
	}


	@Transactional // Tested
	public List<UpdateNotification> getAllUserNotifications(Long userId, int page, int size) {
		return getDao().getAllNotificationsByUserIdOrderDesc(userId, page, size);
	}


	@Transactional // Tested
	public List<UpdateNotification> getAllUserNotifications(Long userId) {
		return getDao().getAllNotificationsByUserIdOrderDesc(userId);
	}


	@Transactional // Tested
	public long countAllUserNotifications(Long userId) {
		return getDao().countUserNotifications(userId);
	}


	@Transactional // Tested
	public void removeAllUserNotifications(Long userId) {
		getDao().removeUserNotifications(userId);
	}


	@Transactional // Tested
	public List<UpdateNotification>  popAllNotifications(Long userId) {
		List<UpdateNotification> all = getAllUserNotifications(userId);
		removeAllUserNotifications(userId);
		return all;
	}



	private static UpdateNotification.Details
	createDetails(SimpleNotification simpleNotification) {

		final UpdateNotification.Details details = new UpdateNotification.Details();
		details.setType(simpleNotification.getType());
		details.setNotification(simpleNotification.getNotification());
		return details;
	}


	private static UpdateNotification
	createNotification(SimpleNotification simpleNotification) {

		final UpdateNotification notification = new UpdateNotification();
		notification.setTargetUserId(simpleNotification.getTargetUserId());
		notification.setDate(new Date(System.currentTimeMillis()));
		notification.setDetails(createDetails(simpleNotification));
		return notification;
	}


	private static void
	sendNotification(SimpMessagingTemplate webSocketMessageTemplate,
					 UpdateNotification updateNotification) {

		final JsonNotificationForm form = new JsonNotificationForm(updateNotification);
		final String user = updateNotification.getTargetUserId().toString();
		webSocketMessageTemplate.convertAndSendToUser(user, WEB_SOCKET_NOTIFICATION_ENDPOINT, form);
	}

}