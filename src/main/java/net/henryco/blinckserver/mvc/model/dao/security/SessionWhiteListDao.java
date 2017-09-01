package net.henryco.blinckserver.mvc.model.dao.security;

import net.henryco.blinckserver.mvc.model.entity.security.SessionWhiteList;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

/**
 * @author Henry on 01/09/17.
 */
public interface SessionWhiteListDao extends BlinckDaoTemplate<SessionWhiteList, Long> {

	void removeAdminSession(String id);
	void removeUserSession(Long id);

	boolean isAdminSessionExists(String id);
	boolean isUserSessionExists(Long id);

}