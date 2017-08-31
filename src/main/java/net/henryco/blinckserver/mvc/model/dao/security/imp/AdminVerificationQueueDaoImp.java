package net.henryco.blinckserver.mvc.model.dao.security.imp;

import net.henryco.blinckserver.mvc.model.dao.security.AdminVerificationQueueDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.model.repository.security.AdminVerificationQueueRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * @author Henry on 31/08/17.
 */
@Repository
public class AdminVerificationQueueDaoImp
		extends BlinckRepositoryProvider<AdminVerificationQueue, String>
		implements AdminVerificationQueueDao {


	@Autowired
	public AdminVerificationQueueDaoImp(AdminVerificationQueueRepository repository) {
		super(repository);
	}

}