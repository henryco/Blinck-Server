package net.henryco.blinckserver.mvc.service.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
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
		extends BlinckDaoProvider<UpdateNotification, Long> {


	@Data @NoArgsConstructor @AllArgsConstructor
	public static final class SimpleNotification {

		private Long targetUserId;
		private String notification;
		private String type;
	}


	@Autowired
	public UpdateNotificationService(UpdateNotificationDao notificationDao) {
		super(notificationDao);
	}

	private UpdateNotificationDao getDao() {
		return provideDao();
	}


	@Transactional
	public void addNotification(SimpleNotification simpleNotification) {
		getDao().save(createNotification(simpleNotification));
	}

	@Transactional
	public void addNotification(Long targetUserId, String type, String notification) {
		addNotification(new SimpleNotification(targetUserId, type, notification));
	}


	@Transactional
	public List<UpdateNotification> getAllUserNotifications(Long userId, int page, int size) {
		return getDao().getAllNotificationsByUserIdOrderDesc(userId, page, size);
	}

	@Transactional
	public List<UpdateNotification> getAllUserNotifications(Long userId) {
		return getDao().getAllNotificationsByUserIdOrderDesc(userId);
	}

	@Transactional
	public long countAllUserNotifications(Long userId) {
		return getDao().countUserNotifications(userId);
	}

	@Transactional
	public void removeAllUserNotifications(Long userId) {
		getDao().removeUserNotifications(userId);
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