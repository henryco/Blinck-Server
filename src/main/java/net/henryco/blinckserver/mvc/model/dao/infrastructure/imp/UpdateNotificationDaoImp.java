package net.henryco.blinckserver.mvc.model.dao.infrastructure.imp;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.UpdateNotificationDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import net.henryco.blinckserver.mvc.model.repository.infrastructure.UpdateNotificationRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 06/09/17.
 */
@Repository
public class UpdateNotificationDaoImp
		extends BlinckRepositoryProvider<UpdateNotification, Long>
		implements UpdateNotificationDao {


	public UpdateNotificationDaoImp(UpdateNotificationRepository repository) {
		super(repository);
	}


	private UpdateNotificationRepository getRepository() {
		return provideRepository();
	}


	@Override @Transactional
	public List<UpdateNotification> getAllNotificationsByUserIdOrderDesc(Long userId, int page, int size) {
		return getRepository().getAllByTargetUserIdOrderByDateDesc(userId, new PageRequest(page, size));
	}

	@Override @Transactional
	public List<UpdateNotification> getAllNotificationsByUserIdOrderDesc(Long userId) {
		return getRepository().getAllByTargetUserIdOrderByDateDesc(userId);
	}

	@Override @Transactional
	public long countUserNotifications(Long userId) {
		return getRepository().countAllByTargetUserId(userId);
	}

	@Override @Transactional
	public void removeUserNotifications(Long userId) {
		getRepository().removeAllByTargetUserId(userId);
	}

}