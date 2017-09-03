package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.AdminVerificationQueueDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

		if (authProfileDao.isExists(name)) return;

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
		try {
			verificationQueueDao.deleteById(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Transactional
	public void addAuthority(String name, String authority) {

		AdminAuthProfile profile = authProfileDao.getById(name);
		List<String> authList = getAuthList(profile);

		if (!authList.contains(authority)) {
			authList.add(authority);
			authProfileDao.save(saveAuthList(profile, authList));
		}
	}


	@Transactional
	public void removeAuthority(String name, String authority) {

		AdminAuthProfile profile = authProfileDao.getById(name);
		List<String> authList = getAuthList(profile);

		if (authList.contains(authority)) {
			authList.remove(authority);
			authProfileDao.save(saveAuthList(profile, authList));
		}
	}


	@Transactional
	public AdminAuthProfile getProfile(String username) {
		if (!authProfileDao.isExists(username))
			return null;
		return authProfileDao.getById(username);
	}


	@Transactional
	public void deleteProfile(String username) {
		if (authProfileDao.isExists(username)) {
			authProfileDao.deleteById(username);
			if (verificationQueueDao.isExists(username))
				verificationQueueDao.deleteById(username);
		}
	}


	@Transactional
	public Collection<AdminAuthProfile> getAdminProfiles() {
		return authProfileDao.getAll();
	}


	@Transactional
	public Collection<AdminVerificationQueue> getVerificationQueue(int n) {
		return verificationQueueDao.getFirst(n);
	}


	@Transactional
	public Collection<AdminVerificationQueue> getVerificationQueue() {
		return verificationQueueDao.getAll();
	}




	private static @BlinckTestName
	AdminAuthProfile createNewAdminProfile(String name, String password) {

		AdminAuthProfile adminAuthProfile = new AdminAuthProfile();
		adminAuthProfile.setId(name);
		adminAuthProfile.setPassword(password);
		adminAuthProfile.setEnabled(false);
		adminAuthProfile.setAuthorityArray(ROLES);
		return adminAuthProfile;
	}


	private static @BlinckTestName
	AdminVerificationQueue createNewAdminVerification(String adminId) {

		AdminVerificationQueue queue = new AdminVerificationQueue();
		queue.setId(adminId);
		queue.setRegistrationTime(new Date(System.currentTimeMillis()));
		return queue;
	}


	private static @BlinckTestName
	List<String> getAuthList(AdminAuthProfile profile) {
		List<String> authList = new LinkedList<>();
		authList.addAll(Arrays.asList(profile.getAuthorityArray()));
		return authList;
	}


	private static @BlinckTestName
	AdminAuthProfile saveAuthList(AdminAuthProfile profile, List<String> authList) {
		profile.setAuthorityArray(authList.toArray(new String[0]));
		return profile;
	}

}