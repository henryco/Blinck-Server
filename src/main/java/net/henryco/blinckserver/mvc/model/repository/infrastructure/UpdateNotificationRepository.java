package net.henryco.blinckserver.mvc.model.repository.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 06/09/17.
 */
public interface UpdateNotificationRepository extends JpaRepository<UpdateNotification, Long> {

	List<UpdateNotification> getAllByTargetUserIdOrderByDateDesc(Long targetUserId, Pageable pageable);
	List<UpdateNotification> getAllByTargetUserIdOrderByDateDesc(Long targetUserId);

	long countAllByTargetUserId(Long targetUserId);

	void removeAllByTargetUserId(Long targetUserId);
}