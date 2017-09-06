package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 06/09/17.
 */
@Service
public class UpdateNotificationService
		extends BlinckDaoProvider<UpdateNotification, Long> {


	@Autowired
	public UpdateNotificationService(UpdateNotificationDao notificationDao) {
		super(notificationDao);
	}

	private UpdateNotificationDao getDao() {
		return provideDao();
	}



}