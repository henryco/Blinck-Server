package net.henryco.blinckserver.mvc.service.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.util.NotificationType;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Henry on 06/09/17.
 */
@Service
public class UpdateNotificationService
		extends BlinckDaoProvider<UpdateNotification, Long>
		implements NotificationType {


	@Data @NoArgsConstructor @AllArgsConstructor
	public static final class SimpleNotification {

		private Long targetUserId;
		private String type;
		private String notification;
	}


	@Autowired
	public UpdateNotificationService(UpdateNotificationDao notificationDao) {
		super(notificationDao);
	}

	private UpdateNotificationDao getDao() {
		return provideDao();
	}


	/**
	 * @return <b>notification ID</b>
	 */
	@Transactional // Tested
	public Long addNotification(SimpleNotification simpleNotification) {
		return getDao().save(createNotification(simpleNotification)).getId();
	}


	/**
	 * @return <b>notification ID</b>
	 */
	@Transactional // Tested
	public Long addNotification(Long targetUserId, String type, String notification) {
		return addNotification(new SimpleNotification(targetUserId, type, notification));
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


}