package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.AdminVerificationQueueDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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
	public void addNewProfile(String name, String password) {

		if (authProfileDao.isExists(name))
			throw new RuntimeException(name + "is already exist!");
		saveProfile(name, password);
	}


	@Transactional
	public void addNewProfileIfNotExist(String name, String password) {

		if (authProfileDao.isExists(name)) return;
		saveProfile(name, password);
	}


	@Transactional
	protected void saveProfile(String name, String password) {

		AdminAuthProfile profile = createNewAdminProfile(name, password);
		AdminVerificationQueue queue = createNewAdminVerification(profile.getId());

		authProfileDao.save(profile);
		verificationQueueDao.save(queue);
	}


	@Transactional
	public void activateProfile(String username) {

		AdminAuthProfile profile = authProfileDao.getById(username);
		profile.setEnabled(true);
		authProfileDao.save(profile);
		verificationQueueDao.deleteByAdminProfileId(username);
	}


	@Transactional
	public void deleteProfile(String username) {
		authProfileDao.deleteById(username);
	}


	@Transactional
	public Collection<AdminVerificationQueue> getVerificationQueue(int size) {
		return verificationQueueDao.getLast(size);
	}


	@Transactional
	public Collection<AdminVerificationQueue> getVerificationQueue() {
		return verificationQueueDao.getAll();
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