package net.henryco.blinckserver.security.token.service;

import net.henryco.blinckserver.mvc.model.dao.security.SessionWhiteListDao;
import net.henryco.blinckserver.mvc.model.entity.security.SessionWhiteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Henry on 01/09/17.
 */
@Service
public class SessionWhiteListService {

	private final SessionWhiteListDao whiteListDao;

	@Autowired
	public SessionWhiteListService(SessionWhiteListDao whiteListDao) {
		this.whiteListDao = whiteListDao;
	}


	@Transactional
	public void addUserToWhiteList(Long userId) {
		if (!isUserInTheWhiteList(userId)) {
			SessionWhiteList session = new SessionWhiteList();
			session.setUserId(userId);
			whiteListDao.save(session);
		}
	}


	@Transactional
	public void addAdminToWhiteList(String adminId) {
		if (!isAdminInTheWhiteList(adminId)) {
			SessionWhiteList session = new SessionWhiteList();
			session.setAdminId(adminId);
			whiteListDao.save(session);
		}
	}


	@Transactional
	public void removeUserFromWhiteList(Long userId) {
		if (isUserInTheWhiteList(userId))
			whiteListDao.removeUserSession(userId);
	}


	@Transactional
	public void removeAdminFromWhiteList(String adminId) {
		if (isAdminInTheWhiteList(adminId))
			whiteListDao.removeAdminSession(adminId);
	}


	@Transactional
	public boolean isUserInTheWhiteList(Long userId) {
		return whiteListDao.isUserSessionExists(userId);
	}


	@Transactional
	public boolean isAdminInTheWhiteList(String adminId) {
		return whiteListDao.isAdminSessionExists(adminId);
	}


	@Transactional
	public Collection<SessionWhiteList> getAllSessions() {
		return whiteListDao.getAll();
	}


	@Transactional
	public Collection<SessionWhiteList> getSessions(int numb) {
		return whiteListDao.getFirst(numb);
	}

}