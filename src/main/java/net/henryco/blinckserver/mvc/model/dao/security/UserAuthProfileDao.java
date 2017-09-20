package net.henryco.blinckserver.mvc.model.dao.security;

import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 23/08/17.
 */
public interface UserAuthProfileDao extends BlinckDaoTemplate<UserAuthProfile, Long> {

	List<UserAuthProfile> getLockedProfiles(int page, int size);
	long countLockedProfiles();
}