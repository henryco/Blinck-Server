package net.henryco.blinckserver.mvc.service.data;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Autowired
	public AdminDataService(AdminAuthProfileDao authProfileDao) {
		this.authProfileDao = authProfileDao;
	}


	@Transactional
	public void addNewAdmin(String name, String password) {

		if (authProfileDao.isExists(name))
			throw new RuntimeException(name + "is already exist!");
		authProfileDao.save(createNewAdminProfile(name, password));
	}


	@Transactional
	public void addNewAdminIfNotExist(String name, String password) {

		if (authProfileDao.isExists(name)) return;
		authProfileDao.save(createNewAdminProfile(name, password));
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


}