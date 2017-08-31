package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.AdminVerificationQueueDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Henry on 31/08/17.
 */
@Service
public class AdminDataService {

	private final static String[] ROLES = {
			"ROLE_ADMIN",
			"ROLE_USER"
	};


	private final AdminAuthProfileDao authProfileDao;
	private final AdminVerificationQueueDao verificationQueueDao;


	@Autowired
	public AdminDataService(AdminVerificationQueueDao verificationQueueDao,
							AdminAuthProfileDao authProfileDao) {
		this.verificationQueueDao = verificationQueueDao;
		this.authProfileDao = authProfileDao;
	}


	@Transactional
	public void addNewAdmin(String name, String password) {

		if (authProfileDao.isExists(name))
			throw new RuntimeException(name + "is already exist!");

		AdminAuthProfile profile = createNewAdminProfile(name, password);
		AdminVerificationQueue queue = createNewAdminVerification(profile.getId());

		authProfileDao.save(profile);
		verificationQueueDao.save(queue);
	}


	@Transactional
	public void addNewAdminIfNotExist(String name, String password) {

		if (authProfileDao.isExists(name)) return;

		AdminAuthProfile profile = createNewAdminProfile(name, password);
		AdminVerificationQueue queue = createNewAdminVerification(profile.getId());

		authProfileDao.save(profile);
		verificationQueueDao.save(queue);
	}


	private static AdminAuthProfile
	createNewAdminProfile(String name, String password) {

		AdminAuthProfile adminAuthProfile = new AdminAuthProfile();
		adminAuthProfile.setId(name);
		adminAuthProfile.setPassword(password);
		adminAuthProfile.setExpired(false);
		adminAuthProfile.setEnabled(false);
		adminAuthProfile.setAuthorityArray(ROLES);
		return adminAuthProfile;
	}


	private static AdminVerificationQueue
	createNewAdminVerification(String adminId) {

		AdminVerificationQueue queue = new AdminVerificationQueue();
		queue.setAdminProfile(adminId);
		queue.setRegistrationTime(new Date(System.currentTimeMillis()));
		return queue;
	}
}