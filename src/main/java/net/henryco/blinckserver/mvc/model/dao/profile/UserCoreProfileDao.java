package net.henryco.blinckserver.mvc.model.dao.profile;

import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 23/08/17.
 */
public interface UserCoreProfileDao extends BlinckDaoTemplate<UserCoreProfile, Long> {

	List<UserCoreProfile> findByUserName(String username, int page, int size);
	UserCoreProfile getByNickName(String nick);
	boolean isNickNameExists(String nick);
}
